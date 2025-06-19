package com.spotteacher.admin.shared.infra

import com.spotteacher.util.logInfo
import com.spotteacher.util.logWarn
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.env.Environment
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Component
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.cloudfront.CloudFrontUtilities
import software.amazon.awssdk.services.cloudfront.model.CannedSignerRequest
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.model.S3Exception
import software.amazon.awssdk.services.s3.model.ServerSideEncryption
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.DeleteObjectPresignRequest
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest
import java.io.InputStream
import java.net.URI
import java.security.KeyFactory
import java.security.spec.PKCS8EncodedKeySpec
import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Base64

/**
 * AWS S3サービスとの連携を行うクライアントクラス。
 * ファイルのアップロード、削除、署名付きURLの生成などの機能を提供します。
 * 環境に応じて、ローカル開発用のMinioまたは本番環境のAWS S3に接続します。
 *
 * @param environment 現在の実行環境情報
 * @param resourceLoader リソースを読み込むためのローダー
 */
@Component
class S3Client(private val environment: Environment, private val resourceLoader: ResourceLoader) {
    @Value("\${aws.s3.bucket}")
    lateinit var bucketName: String
    private val region = Region.AP_NORTHEAST_1
    private val cloudFrontUtilities = CloudFrontUtilities.create()

    @Value("\${aws.cloudfront.distribution-domain}")
    private lateinit var distributionDomain: String

    @Value("\${aws.cloudfront.key-pair-id}")
    private lateinit var keyPairId: String

    @Value("\${aws.cloudfront.private-key}")
    private lateinit var privateKeyString: String

    private val oneHourUrlExpirationSeconds: Long = 1

    private val client = if (environment.matchesProfiles("local")) {
        S3Client.builder()
            .region(Region.US_EAST_1)
            .endpointOverride(URI.create("http://localhost:9010"))
            .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create("minio", "password")))
            .build()
    } else {
        S3Client.builder()
            .region(region)
            .credentialsProvider(DefaultCredentialsProvider.create())
            .build()
    }

    private val s3Presigner = if (environment.matchesProfiles("local")) {
        S3Presigner.builder()
            .region(region)
            .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create("minio", "password")))
            .build()
    } else {
        S3Presigner.builder()
            .region(region)
            .credentialsProvider(DefaultCredentialsProvider.create())
            .build()
    }

    /**
     * S3バケットにファイルをアップロードします。
     *
     * @param fileKey アップロードするファイルのキー（パス）
     * @param fileStream アップロードするファイルのInputStream
     * @throws S3Exception アップロード処理中にエラーが発生した場合
     */
    fun upload(fileKey: String, fileStream: InputStream) {
        try {
            val putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileKey)
                .serverSideEncryption(ServerSideEncryption.AES256)
                .build()
            client
                .putObject(putObjectRequest, RequestBody.fromInputStream(fileStream, fileStream.available().toLong()))
        } catch (e: S3Exception) {
            logWarn { "Failed to upload file: $fileKey error: ${e.message}" }
            throw e
        }
    }

    /**
     * S3バケットからファイルを削除します。
     *
     * @param fileKey 削除するファイルのキー（パス）
     * @throws S3Exception 削除処理中にエラーが発生した場合
     */
    fun delete(fileKey: String) {
        try {
            val request = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(fileKey)
                .build()
            client.deleteObject(request)
        } catch (e: S3Exception) {
            logWarn { "Failed to delete file: $fileKey error: ${e.message}" }
            throw e
        }
    }

    /**
     * S3バケットにファイルをアップロードするための署名付きURLを生成します。
     * このURLを使用することで、クライアントは直接S3バケットにファイルをアップロードできます。
     *
     * @param fileKey アップロードするファイルのキー（パス）
     * @param expirationMinutes URLの有効期限（分）、デフォルトは10分
     * @return 署名付きアップロードURL
     * @throws S3Exception URL生成処理中にエラーが発生した場合
     */
    fun generateUploadUrl(fileKey: String, expirationMinutes: Long = 10): String {
        return try {
            val request = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(expirationMinutes))
                .putObjectRequest { req ->
                    req.bucket(bucketName).key(fileKey).serverSideEncryption(ServerSideEncryption.AES256)
                }
                .build()

            s3Presigner.presignPutObject(request).url().toString()
        } catch (e: S3Exception) {
            logWarn { "Failed to generate upload url: $fileKey error: ${e.message}" }
            throw e
        }
    }

    /**
     * S3バケットからファイルを削除するための署名付きURLを生成します。
     * このURLを使用することで、クライアントは直接S3バケットからファイルを削除できます。
     *
     * @param key 削除するファイルのキー（パス）
     * @param expirationMinutes URLの有効期限（分）、デフォルトは10分
     * @return 署名付き削除URL
     * @throws S3Exception URL生成処理中にエラーが発生した場合
     */
    fun generateDeleteUrl(key: String, expirationMinutes: Long = 10): String {
        return try {
            val request = DeleteObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(expirationMinutes))
                .deleteObjectRequest { req ->
                    req.bucket(bucketName).key(key)
                }
                .build()

            s3Presigner.presignDeleteObject(request).url().toString()
        } catch (e: S3Exception) {
            logWarn { "Failed to generate delete url: $key error: ${e.message}" }
            throw e
        }
    }

    /**
     * S3バケットからファイルをダウンロードするためのURLを生成します。
     * ローカル環境ではMinioの署名付きS3 URLを生成し、本番環境ではCloudFrontのURLを生成します。
     * ファイルのパスに応じて、キャッシュ設定や署名付きURLの生成方法が異なります。
     *
     * @param key ダウンロードするファイルのキー（パス）
     * @return ダウンロードURL
     * @throws S3Exception URL生成処理中にエラーが発生した場合
     */
    fun generateDownloadUrl(key: String): String {
        return if (environment.matchesProfiles("local")) {
            try {
                val request = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(10)) // 署名の有効時間
                    .getObjectRequest { req ->
                        req.bucket(bucketName).key(key)
                    }
                    .build()

                s3Presigner.presignGetObject(request).url().toString()
            } catch (e: S3Exception) {
                logWarn { "Failed to generate download url: $key error: ${e.message}" }
                throw e
            }
        } else {
            try {
                when {
                    // １時間限定の署名付きキャッシュのファイルの場合
                    key.startsWith("one-hour-cache/") -> generateSignedCacheUrl(
                        key,
                        oneHourUrlExpirationSeconds
                    )
                    // キャッシュ無しの署名付きファイルの場合
                    key.endsWith("no-cache/") -> generateSignedCacheUrl(key, 0)
                    else -> key
                }
            } catch (e: S3Exception) {
                logWarn { "Failed to generate url: $key error: ${e.message}" }
                throw e
            }
        }
    }

    /**
     * CloudFrontの署名付きURLを生成します。
     * 指定されたTTL（Time To Live）に基づいて有効期限を設定します。
     *
     * @param key リソースのキー（パス）
     * @param ttlHour URLの有効期限（時間）
     * @return CloudFrontの署名付きURL
     * @throws Exception URL生成処理中にエラーが発生した場合
     */
    private fun generateSignedCacheUrl(key: String, ttlHour: Long): String {
        return try {
            val resourceUrl = "$distributionDomain/$key"
            // 現在時刻を取得し、時間を追加した後、時間単位に切り捨てる（分、秒、ナノ秒を0に設定）
            val now = Instant.now()
            val withAddedHours = now.plus(ttlHour, ChronoUnit.HOURS)
            val expirationDate = withAddedHours.truncatedTo(ChronoUnit.HOURS)

            val pem = privateKeyString
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replace("\\s".toRegex(), "") // 改行を削除

            val decoded = Base64.getDecoder().decode(pem)
            val keySpec = PKCS8EncodedKeySpec(decoded)
            val privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpec)

            // CannedSignerRequestを作成する
            val cannedRequest = CannedSignerRequest.builder().resourceUrl(resourceUrl)
                .privateKey(privateKey).keyPairId(keyPairId)
                .expirationDate(expirationDate).build()

            // 署名付きURLを生成する
            val signedUrl = cloudFrontUtilities.getSignedUrlWithCannedPolicy(cannedRequest).url()
            logInfo { "Signed url: $signedUrl" } // todo : RW-1168/PoC用 削除する
            signedUrl
        } catch (e: Exception) {
            logWarn { "Failed to generate url: $key error: ${e.message}" }
            throw e
        }
    }
}

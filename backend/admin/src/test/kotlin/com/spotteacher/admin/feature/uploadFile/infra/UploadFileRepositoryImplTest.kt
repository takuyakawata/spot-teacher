package com.spotteacher.admin.feature.uploadFile.infra

import arrow.core.nonEmptyListOf
import com.spotteacher.admin.feature.uploadFile.domain.FileKey
import com.spotteacher.admin.feature.uploadFile.domain.UploadFile
import com.spotteacher.admin.feature.uploadFile.domain.UploadFileId
import com.spotteacher.admin.feature.uploadFile.domain.UploadFileRepository
import com.spotteacher.admin.feature.uploadFile.domain.UploadStatus
import com.spotteacher.admin.fixture.UploadFileFixture
import com.spotteacher.backend.DatabaseDescribeSpec
import com.spotteacher.infra.db.tables.references.UPLOAD_FILES
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.r2dbc.core.awaitOne
import org.springframework.test.context.TestPropertySource

@SpringBootTest(webEnvironment = NONE)
@TestPropertySource(properties = ["aws.s3.bucket=test-bucket"])
class UploadFileRepositoryImplTest(
    private val repository: UploadFileRepository,
    override var databaseClient: DatabaseClient,
    private val uploadFileFixture: UploadFileFixture,
) : DatabaseDescribeSpec({
    describe("UploadFileRepositoryImpl") {
        describe("create") {
            it("should create a new UploadFile successfully") {
                val uploadFile = uploadFileFixture.buildWithFileKey(
                    fileKey = FileKey("test/file-key.jpg"),
                )

                val actual = repository.create(uploadFile)

                actual.id shouldNotBe UploadFileId(0L)
                actual.fileKey shouldBe uploadFile.fileKey
                actual.status shouldBe UploadStatus.PENDING
            }
        }

        describe("bulkCreate") {
            it("should create multiple upload files") {
                val files = nonEmptyListOf(
                    uploadFileFixture.buildWithFileKey(
                        fileKey = FileKey("test/bulk-create-1.jpg")
                    ),
                    uploadFileFixture.buildWithFileKey(
                        fileKey = FileKey("test/bulk-create-2.jpg")
                    )
                )

                val actual = repository.bulkCreate(files)

                actual.size shouldBe 2
                actual[0].fileKey shouldBe files[0].fileKey
                actual[0].status shouldBe UploadStatus.PENDING
                actual[1].fileKey shouldBe files[1].fileKey
                actual[1].status shouldBe UploadStatus.PENDING
            }
        }

        describe("findById") {
            context("when the upload file exists") {
                it("should return the upload file") {
                    val uploadFile = uploadFileFixture.createWithFileKey(
                        fileKey = FileKey("test/file-key.jpg"),
                    )

                    val actual = repository.findById(uploadFile.id)

                    actual shouldNotBe null
                    actual!!.id shouldBe uploadFile.id
                    actual.fileKey shouldBe FileKey("test/file-key.jpg")
                    actual.status shouldBe UploadStatus.PENDING
                }
            }

            context("when the upload file does not exist") {
                it("should return null") {
                    val actual = repository.findById(UploadFileId(999L))
                    actual shouldBe null
                }
            }
        }

        describe("filterByIds") {
            it("should return upload files with matching ids") {
                val uploadFile1 = uploadFileFixture.createWithFileKey(
                    fileKey = FileKey("test/file-key-1.jpg")
                )
                val uploadFile2 = uploadFileFixture.createUploadedFile(
                    fileKey = FileKey("test/file-key-2.jpg")
                )

                val ids = nonEmptyListOf(uploadFile1.id, uploadFile2.id)

                val actual = repository.filterByIds(ids)

                actual.size shouldBe 2
                actual[0].apply {
                    id shouldBe uploadFile1.id
                    fileKey shouldBe uploadFile1.fileKey
                    status shouldBe UploadStatus.PENDING
                }
                actual[1].apply {
                    id shouldBe uploadFile2.id
                    fileKey shouldBe uploadFile2.fileKey
                    status shouldBe UploadStatus.UPLOADED
                }
            }
        }

        describe("generateUploadUrl") {
            it("should generate upload URL using S3Client") {
                val fileKey = FileKey("test/new-file.jpg")

                val actual = repository.generateUploadUrl(fileKey)

                // Just verify the URL contains the bucket name and is a valid S3 URL
                actual shouldContain "test-bucket.s3"
            }
        }

        describe("generateDownloadUrl") {
            it("should generate download URL using S3Client") {
                val fileKey = FileKey("test/new-file.jpg")

                val actual = repository.generateDownloadUrl(fileKey)

                // Just verify the URL contains the bucket name and is a valid S3 URL
                actual shouldContain "test-bucket.s3"
            }
        }

        describe("upload") {
            it("should change the status to UPLOADED") {
                // Create a file in PENDING status
                val uploadFile = uploadFileFixture.createWithFileKey(
                    fileKey = FileKey("test/upload-method.jpg")
                )

                // Initial status should be PENDING
                uploadFile.status shouldBe UploadStatus.PENDING

                // Call upload() method
                val uploadedFile = uploadFile.upload()

                // Status should be changed to UPLOADED
                uploadedFile.status shouldBe UploadStatus.UPLOADED

                // Other properties should remain the same
                uploadedFile.id shouldBe uploadFile.id
                uploadedFile.fileKey shouldBe uploadFile.fileKey
            }
        }

        describe("bulkUpdate") {
            it("should update multiple upload files status") {
                // Create test files in PENDING status
                val uploadFile1 = uploadFileFixture.createWithFileKey(
                    fileKey = FileKey("test/bulk-update-1.jpg")
                )
                val uploadFile2 = uploadFileFixture.createWithFileKey(
                    fileKey = FileKey("test/bulk-update-2.jpg")
                )

                // Update status to UPLOADED
                val filesToUpdate = nonEmptyListOf(
                    uploadFile1.copy(status = UploadStatus.UPLOADED),
                    uploadFile2.copy(status = UploadStatus.UPLOADED)
                )
                repository.bulkUpdate(filesToUpdate)

                // Verify database state
                val updatedFilesCount = databaseClient.sql(
                    """
                    SELECT COUNT(*) as count
                    FROM ${UPLOAD_FILES.name}
                    WHERE id IN (:id1, :id2)
                    AND upload_status = :status
                """
                )
                    .bind("id1", uploadFile1.id.value)
                    .bind("id2", uploadFile2.id.value)
                    .bind("status", "UPLOADED")
                    .map { row -> row.get("count", java.lang.Long::class.java)?.toLong() ?: 0L }
                    .awaitOne()

                updatedFilesCount shouldBe 2L
            }
        }

        describe("bulkDelete") {
            it("should delete multiple upload files by IDs") {
                // テスト用ファイルを作成
                val uploadFile1 = uploadFileFixture.createWithFileKey(
                    fileKey = FileKey("test/bulk-delete-1.jpg")
                )
                val uploadFile2 = uploadFileFixture.createWithFileKey(
                    fileKey = FileKey("test/bulk-delete-2.jpg")
                )

                // 削除対象のIDリストを作成
                val ids = nonEmptyListOf(uploadFile1.id, uploadFile2.id)

                // 実行
                repository.bulkDelete(ids)

                // 削除後の検証
                val remainingFilesCount = databaseClient.sql(
                    """
                    SELECT COUNT(*) as count
                    FROM ${UPLOAD_FILES.name}
                    WHERE id IN (:id1, :id2)
                """
                )
                    .bind("id1", uploadFile1.id.value)
                    .bind("id2", uploadFile2.id.value)
                    .map { row -> row.get("count", Long::class.java) ?: 0L }
                    .awaitOne()

                remainingFilesCount shouldBe 0L
            }
        }

        describe("edge cases") {
            describe("FileKey validation") {
                it("should handle long file keys correctly") {
                    val longFileKey = FileKey("test/" + "a".repeat(200) + ".jpg")
                    val uploadFile = uploadFileFixture.buildWithFileKey(fileKey = longFileKey)

                    val actual = repository.create(uploadFile)

                    actual.fileKey shouldBe longFileKey
                    actual.status shouldBe UploadStatus.PENDING
                }
            }

            describe("UploadStatus transitions") {
                it("should handle status changes correctly") {
                    val uploadFile = uploadFileFixture.createWithFileKey(
                        fileKey = FileKey("test/status-change.jpg"),
                        status = UploadStatus.PENDING
                    )

                    // Test upload() method
                    val uploadedFile = uploadFile.upload()
                    uploadedFile.status shouldBe UploadStatus.UPLOADED

                    // Verify original file status unchanged
                    uploadFile.status shouldBe UploadStatus.PENDING
                }
            }
        }
    }
})

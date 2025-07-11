package com.spotteacher.teacher.feature.uploadFile.infra

import arrow.core.Nel
import com.spotteacher.extension.nonBlockingFetch
import com.spotteacher.infra.db.tables.UploadFiles.Companion.UPLOAD_FILES
import com.spotteacher.infra.db.tables.records.UploadFilesRecord
import com.spotteacher.teacher.feature.uploadFile.domain.FileKey
import com.spotteacher.teacher.feature.uploadFile.domain.UploadFile
import com.spotteacher.teacher.feature.uploadFile.domain.UploadFileId
import com.spotteacher.teacher.feature.uploadFile.domain.UploadFileRepository
import com.spotteacher.teacher.feature.uploadFile.domain.UploadStatus
import com.spotteacher.teacher.shared.infra.S3Client
import com.spotteacher.teacher.shared.infra.TransactionAwareDSLContext
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.springframework.stereotype.Repository

@Repository
class UploadFileRepositoryImpl(
    private val dslContext: TransactionAwareDSLContext,
    private val s3Client: S3Client,
) : UploadFileRepository {
    override suspend fun findById(id: UploadFileId): UploadFile? {
        return dslContext.get().selectFrom(UPLOAD_FILES)
            .where(UPLOAD_FILES.ID.eq(id.value))
            .awaitFirstOrNull()
            ?.toEntity()
    }

    override suspend fun filterByIds(ids: Nel<UploadFileId>): List<UploadFile> {
        return dslContext.get().nonBlockingFetch(
            UPLOAD_FILES,
            UPLOAD_FILES.ID.`in`(ids.map { it.value })
        ).map { it.toEntity() }
    }

    override suspend fun generateDownloadUrl(fileKey: FileKey): String {
        return s3Client.generateDownloadUrl(fileKey.value)
    }

    private fun UploadFilesRecord.toEntity():UploadFile {
        return UploadFile(
            id = UploadFileId(id!!),
            fileKey = FileKey(fileKey),
            status = UploadStatus.valueOf(uploadStatus.name),
        )
    }
}

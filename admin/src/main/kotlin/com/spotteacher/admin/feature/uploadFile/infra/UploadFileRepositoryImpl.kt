package com.spotteacher.admin.feature.uploadFile.infra

import arrow.core.Nel
import com.spotteacher.admin.feature.uploadFile.domain.FileKey
import com.spotteacher.admin.feature.uploadFile.domain.UploadFile
import com.spotteacher.admin.feature.uploadFile.domain.UploadFileId
import com.spotteacher.admin.feature.uploadFile.domain.UploadFileRepository
import com.spotteacher.admin.feature.uploadFile.domain.UploadStatus
import com.spotteacher.admin.shared.infra.S3Client
import com.spotteacher.admin.shared.infra.TransactionAwareDSLContext
import com.spotteacher.extension.nonBlockingFetch
import com.spotteacher.infra.db.enums.UploadFilesUploadStatus
import com.spotteacher.infra.db.tables.UploadFiles.Companion.UPLOAD_FILES
import com.spotteacher.infra.db.tables.records.UploadFilesRecord
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitLast
import org.springframework.stereotype.Repository

@Repository
class UploadFileRepositoryImpl(
    private val dslContext: TransactionAwareDSLContext,
    private val s3Client: S3Client,
) : UploadFileRepository {
    override suspend fun create(uploadFile: UploadFile): UploadFile {
        val id = dslContext.get().insertInto(
            UPLOAD_FILES,
            UPLOAD_FILES.FILE_KEY,
            UPLOAD_FILES.UPLOAD_STATUS,
        ).values(
            uploadFile.fileKey.value,
            UploadFilesUploadStatus.valueOf(uploadFile.status.name)
        ).returning(UPLOAD_FILES.ID)
            .awaitFirstOrNull()?.id!!

        return uploadFile.copy(id = UploadFileId(id))
    }

    override suspend fun bulkCreate(files: Nel<UploadFile>): List<UploadFile> {
        val queries = files.map {
            dslContext.get().insertInto(
                UPLOAD_FILES,
                UPLOAD_FILES.FILE_KEY,
                UPLOAD_FILES.UPLOAD_STATUS,
            ).values(
                it.fileKey.value,
                UploadFilesUploadStatus.valueOf(it.status.name)
            )
        }
        dslContext.get().batch(queries).awaitLast()

        return dslContext.get().nonBlockingFetch(
            UPLOAD_FILES,
            UPLOAD_FILES.FILE_KEY.`in`(files.map { it.fileKey.value }),
        ).toList()
            .map { it.toEntity() }
    }

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

    override suspend fun bulkUpdate(files: Nel<UploadFile>) {
        val queries = files.map {
            dslContext.get().update(UPLOAD_FILES)
                .set(
                    UPLOAD_FILES.UPLOAD_STATUS,
                    UploadFilesUploadStatus.valueOf(it.status.name)
                )
                .where(UPLOAD_FILES.ID.eq(it.id.value))
        }

        dslContext.get().batch(queries).awaitLast()
    }

    override suspend fun generateUploadUrl(fileKey: FileKey): String {
        return s3Client.generateUploadUrl(fileKey.value)
    }

    override suspend fun generateDownloadUrl(fileKey: FileKey): String {
        return s3Client.generateDownloadUrl(fileKey.value)
    }

    override suspend fun bulkDelete(ids: Nel<UploadFileId>) {
        val queries = ids.map {
            dslContext.get().deleteFrom(UPLOAD_FILES)
                .where(UPLOAD_FILES.ID.eq(it.value))
        }

        dslContext.get().batch(queries).awaitLast()
    }

    override suspend fun deleteFile(fileKey: FileKey) {
        s3Client.delete(fileKey.value)
    }

    private fun UploadFilesRecord.toEntity(): UploadFile {
        return UploadFile(
            id = UploadFileId(id!!),
            fileKey = FileKey(fileKey),
            status = UploadStatus.valueOf(uploadStatus.name),
        )
    }
}

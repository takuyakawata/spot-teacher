package com.spotteacher.admin.fixture

import com.spotteacher.admin.feature.uploadFile.domain.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class UploadFileFixture {

    @Autowired
    private lateinit var repository: UploadFileRepository

    private var idCount = 1L

    fun buildUploadFile(
        id: UploadFileId = UploadFileId(idCount++),
        fileKey: FileKey = FileKey("test/file-${System.currentTimeMillis()}.jpg"),
        status: UploadStatus = UploadStatus.PENDING,
    ): UploadFile {
        return UploadFile(
            id = id,
            fileKey = fileKey,
            status = status
        )
    }

    fun buildWithFileKey(
        fileKey: FileKey,
        status: UploadStatus = UploadStatus.PENDING,
    ): UploadFile {
        return UploadFile(
            id = UploadFileId(0L), // Will be set by repository
            fileKey = fileKey,
            status = status
        )
    }

    suspend fun createUploadFile(
        fileKey: FileKey = FileKey("test/file-${System.currentTimeMillis()}.jpg"),
        status: UploadStatus = UploadStatus.PENDING,
    ): UploadFile {
        return repository.create(
            buildWithFileKey(
                fileKey = fileKey,
                status = status,
            )
        )
    }

    suspend fun createWithFileKey(
        fileKey: FileKey,
        status: UploadStatus = UploadStatus.PENDING,
    ): UploadFile {
        return repository.create(
            buildWithFileKey(
                fileKey = fileKey,
                status = status,
            )
        )
    }

    suspend fun createUploadedFile(
        fileKey: FileKey,
    ): UploadFile {
        return repository.create(
            buildWithFileKey(
                fileKey = fileKey,
                status = UploadStatus.UPLOADED,
            )
        )
    }
}
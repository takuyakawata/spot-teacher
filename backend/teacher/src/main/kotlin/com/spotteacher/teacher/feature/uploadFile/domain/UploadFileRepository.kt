package com.spotteacher.teacher.feature.uploadFile.domain

import arrow.core.Nel

interface UploadFileRepository {
    suspend fun findById(id: UploadFileId): UploadFile?
    suspend fun filterByIds(ids: Nel<UploadFileId>): List<UploadFile>

    /**
     * This method is used to create a signed URL for uploading files to S3.
     * It does not affect the database.
     */
    suspend fun generateDownloadUrl(fileKey: FileKey): String
}

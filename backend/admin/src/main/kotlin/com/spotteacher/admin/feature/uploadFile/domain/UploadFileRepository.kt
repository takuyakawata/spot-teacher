package com.spotteacher.admin.feature.uploadFile.domain

import arrow.core.Nel

interface UploadFileRepository {
    suspend fun create(uploadFile: UploadFile): UploadFile
    suspend fun bulkCreate(files: Nel<UploadFile>): List<UploadFile>
    suspend fun findById(id: UploadFileId): UploadFile?
    suspend fun filterByIds(ids: Nel<UploadFileId>): List<UploadFile>
    suspend fun bulkUpdate(files: Nel<UploadFile>)
    suspend fun bulkDelete(ids: Nel<UploadFileId>)

    /**
     * This method is used to create a signed URL for uploading files to S3.
     * It does not affect the database.
     */
    suspend fun generateUploadUrl(fileKey: FileKey): String
    suspend fun generateDownloadUrl(fileKey: FileKey): String
    suspend fun deleteFile(fileKey: FileKey)
}

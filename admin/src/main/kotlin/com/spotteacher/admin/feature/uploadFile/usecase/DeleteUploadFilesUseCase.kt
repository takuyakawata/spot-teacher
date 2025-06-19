package com.spotteacher.admin.feature.uploadFile.usecase

import arrow.core.Nel
import com.spotteacher.admin.feature.uploadFile.domain.DeleteUploadFileService
import com.spotteacher.admin.feature.uploadFile.domain.UploadFileId
import com.spotteacher.admin.feature.uploadFile.domain.UploadFileRepository
import com.spotteacher.usecase.UseCase

@UseCase
class DeleteUploadFilesUseCase(
    private val service: DeleteUploadFileService,
    private val uploadFileRepository: UploadFileRepository,
) {
    suspend fun call(ids: Nel<UploadFileId>) {
        val files = uploadFileRepository.filterByIds(ids)
        service.delete(ids)
        files.forEach { file ->
            uploadFileRepository.deleteFile(file.fileKey)
        }
    }
}

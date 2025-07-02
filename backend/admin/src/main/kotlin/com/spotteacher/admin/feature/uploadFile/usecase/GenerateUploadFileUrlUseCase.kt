package com.spotteacher.admin.feature.uploadFile.usecase

import com.spotteacher.admin.feature.uploadFile.domain.UploadFileId
import com.spotteacher.admin.feature.uploadFile.domain.UploadFileRepository
import com.spotteacher.usecase.UseCase

data class GenerateUploadFileUrlUseCaseInput(
    val uploadFileId: UploadFileId,
)

data class GenerateUploadFileUrlUseCaseOutput(
    val url: String,
)

@UseCase
class GenerateUploadFileUrlUseCase(
    private val uploadFileRepository: UploadFileRepository
) {
    class UploadFileNotFoundException(id: UploadFileId) :
        RuntimeException("Upload file not found. id=$id")

    suspend fun call(input: GenerateUploadFileUrlUseCaseInput): GenerateUploadFileUrlUseCaseOutput {
        val uploadFile = uploadFileRepository.findById(input.uploadFileId)
            ?: throw UploadFileNotFoundException(input.uploadFileId)

        val url = uploadFileRepository.generateUploadUrl(uploadFile.fileKey)
        return GenerateUploadFileUrlUseCaseOutput(url)
    }
}

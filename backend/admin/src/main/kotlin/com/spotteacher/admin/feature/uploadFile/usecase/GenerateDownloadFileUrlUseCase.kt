package com.spotteacher.admin.feature.uploadFile.usecase

import com.spotteacher.admin.feature.uploadFile.domain.UploadFileId
import com.spotteacher.admin.feature.uploadFile.domain.UploadFileRepository
import com.spotteacher.exception.ResourceNotFoundException
import com.spotteacher.usecase.UseCase

data class GenerateDownloadFileUrlUseCaseInput(
    val uploadFileId: UploadFileId,
)

data class GenerateDownloadFileUrlUseCaseOutput(
    val url: String,
)

@UseCase
class GenerateDownloadFileUrlUseCase(
    private val uploadFileRepository: UploadFileRepository
) {
    suspend fun call(input: GenerateDownloadFileUrlUseCaseInput): GenerateDownloadFileUrlUseCaseOutput {
        val uploadFile = uploadFileRepository.findById(input.uploadFileId)
            ?: throw ResourceNotFoundException(
                clazz = UploadFileId::class,
                params = mapOf("id" to input.uploadFileId.value),
            )

        val url = uploadFileRepository.generateDownloadUrl(uploadFile.fileKey)
        return GenerateDownloadFileUrlUseCaseOutput(url)
    }
}

package  com.spotteacher.admin.feature.uploadFile.usecase

import arrow.core.Nel
import com.spotteacher.admin.feature.uploadFile.domain.ContentType
import com.spotteacher.admin.feature.uploadFile.domain.UploadFile
import com.spotteacher.admin.feature.uploadFile.domain.UploadFileDirectoryFeatureName
import com.spotteacher.admin.feature.uploadFile.domain.UploadFileRepository
import com.spotteacher.admin.shared.infra.TransactionCoroutine
import com.spotteacher.usecase.UseCase


data class CreateUploadFilesUseCaseInput(
    val fileInputs: Nel<UploadFileInput>
)

data class UploadFileInput(
    val contentType: ContentType,
    val featureName: UploadFileDirectoryFeatureName,
)

data class CreateUploadFilesUseCaseOutput(
    val uploadFiles: List<UploadFile>,
)

@UseCase
class CreateUploadFilesUseCase(
    private val uploadFileRepository: UploadFileRepository,
) {
    @TransactionCoroutine
    suspend fun call(input: CreateUploadFilesUseCaseInput): CreateUploadFilesUseCaseOutput {
        val uploadFiles = input.fileInputs.map { fileInput ->
            UploadFile.create(
                uploadFileDirectoryFeatureName = fileInput.featureName,
                contentType = fileInput.contentType,
            )
        }

        return CreateUploadFilesUseCaseOutput(uploadFileRepository.bulkCreate(uploadFiles))
    }
}

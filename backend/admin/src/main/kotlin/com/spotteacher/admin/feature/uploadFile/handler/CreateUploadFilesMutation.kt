package com.spotteacher.admin.feature.uploadFile.handler

import arrow.core.toNonEmptyListOrNull
import com.expediagroup.graphql.server.operations.Mutation
import com.spotteacher.admin.feature.uploadFile.domain.ContentType
import com.spotteacher.admin.feature.uploadFile.domain.UploadFileDirectoryFeatureName
import com.spotteacher.admin.feature.uploadFile.usecase.CreateUploadFilesUseCase
import com.spotteacher.admin.feature.uploadFile.usecase.CreateUploadFilesUseCaseInput
import com.spotteacher.admin.feature.uploadFile.usecase.UploadFileInput
import com.spotteacher.graphql.NonEmptyString
import graphql.schema.DataFetchingEnvironment
import org.springframework.stereotype.Component

data class CreateUploadFilesMutationInput(
    val fileInputs: List<CreateUploadFileInput>
)

data class CreateUploadFileInput(
    val contentType: NonEmptyString,
    val featureName: UploadFileDirectoryFeatureName,
)

@Component
class CreateUploadFilesMutation(
    private val usecase: CreateUploadFilesUseCase,
) : Mutation {
    suspend fun createUploadFiles(
        input: CreateUploadFilesMutationInput,
        environment: DataFetchingEnvironment,
    ): List<UploadFileType> {
        return usecase.call(
            CreateUploadFilesUseCaseInput(
                fileInputs = input.fileInputs.map {
                    UploadFileInput(
                        contentType = ContentType.from(it.contentType.value),
                        featureName = it.featureName
                    )
                }.toNonEmptyListOrNull()!!
            )
        ).uploadFiles.map {
            UploadFileType(
                id = it.id.toGraphQLId(),
                fileKey = it.fileKey.value,
                uploadStatus = it.status
            )
        }
    }
}

package com.spotteacher.admin.feature.uploadFile.handler

import arrow.core.toNonEmptyListOrNull
import com.expediagroup.graphql.generator.scalars.ID
import com.expediagroup.graphql.server.operations.Mutation
import com.spotteacher.admin.feature.uploadFile.domain.UploadFileId
import com.spotteacher.admin.feature.uploadFile.usecase.DeleteUploadFilesUseCase
import com.spotteacher.graphql.toDomainId
import org.springframework.stereotype.Component

data class DeleteUploadFilesMutationInput(
    val uploadFileIDs: List<ID>,
)

@Component
class DeleteUploadFilesMutation(private val usecase: DeleteUploadFilesUseCase) : Mutation {

    suspend fun deleteUploadFiles(
        input: DeleteUploadFilesMutationInput,
    ) {
        val uploadFileIds = input.uploadFileIDs.map { it.toDomainId { UploadFileId(it) } }.toNonEmptyListOrNull()
            ?: throw IllegalArgumentException("uploadFileIDs must not be empty")
        usecase.call(
            uploadFileIds,
        )
    }
}

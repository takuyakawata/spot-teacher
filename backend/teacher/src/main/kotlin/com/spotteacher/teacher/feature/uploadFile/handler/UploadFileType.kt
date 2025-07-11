package com.spotteacher.teacher.feature.uploadFile.handler

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.expediagroup.graphql.generator.annotations.GraphQLName
import com.expediagroup.graphql.generator.scalars.ID
import com.spotteacher.graphql.toDomainId
import com.spotteacher.graphql.toID
import com.spotteacher.teacher.feature.uploadFile.domain.UploadFileId
import com.spotteacher.teacher.feature.uploadFile.domain.UploadStatus
import com.spotteacher.teacher.feature.uploadFile.usecase.GenerateDownloadFileUrlUseCase
import com.spotteacher.teacher.feature.uploadFile.usecase.GenerateDownloadFileUrlUseCaseInput
import org.springframework.beans.factory.annotation.Autowired

const val UPLOAD_FILE_TYPE_GRAPHQL_NAME = "uploadFile"

@GraphQLName(UPLOAD_FILE_TYPE_GRAPHQL_NAME)
data class UploadFileType(
    val id: ID,
    @GraphQLIgnore
    val fileKey: String,
    val uploadStatus: UploadStatus,
) {
    suspend fun generateDownloadFileUrl(
        @GraphQLIgnore
        @Autowired
        useCase: GenerateDownloadFileUrlUseCase,
    ): String {
        return useCase.call(
            GenerateDownloadFileUrlUseCaseInput(
                uploadFileId = id.toDomainId(::UploadFileId),
            )
        ).url
    }
}

fun UploadFileId.toGraphQLId(): ID = this.toID(UPLOAD_FILE_TYPE_GRAPHQL_NAME)

package com.spotteacher.admin.feature.uploadFile.handler

import com.expediagroup.graphql.generator.annotations.GraphQLName
import java.time.LocalDateTime

const val UPLOAD_FILE_DOWNLOAD_URL_TYPE_GRAPHQL_NAME = "UploadFileDownloadUrl"

@GraphQLName(UPLOAD_FILE_DOWNLOAD_URL_TYPE_GRAPHQL_NAME)
data class UploadFileDownloadUrlType(
    val downloadUrl: String,
    val expiredAt: LocalDateTime,
)

package com.spotteacher.admin.feature.uploadFile.domain

import com.spotteacher.util.Identity

data class UploadFile(
    val id: UploadFileId,
    val key: String,
)

class UploadFileId(override val value: Long) : Identity<Long>(value)

package com.spotteacher.admin.feature.uploadFile.domain

import com.spotteacher.util.Identity
import java.util.*

class UploadFileId(override val value: Long) : Identity<Long>(value)

@JvmInline
value class FileKey(val value: String) {
    init {
        require(value.isNotBlank()) { "File key must not be blank" }
        require(value.length <= MAX_KEY_LENGTH) { "File key must not exceed $MAX_KEY_LENGTH characters" }
    }
    companion object {
        const val MAX_KEY_LENGTH = 255
        fun from(
            directoryFeatureName: UploadFileDirectoryFeatureName,
            contentType: ContentType,
        ): FileKey {
            return when (directoryFeatureName) {
                UploadFileDirectoryFeatureName.REPORT -> {
                    FileKey("no-cache/${directoryFeatureName.name}/${UUID.randomUUID()}.$contentType")
                }

                UploadFileDirectoryFeatureName.WORK_TEMPLATE,
                UploadFileDirectoryFeatureName.WORK_PLAN,
                UploadFileDirectoryFeatureName.WORK_TICKET,
                UploadFileDirectoryFeatureName.REAL_ESTATE,
                -> {
                    FileKey("one-hour-cache/${directoryFeatureName.name}/${UUID.randomUUID()}.$contentType")
                }
            }
        }
    }
}

data class UploadFile(
    val id: UploadFileId,
    val fileKey: FileKey,
    val status: UploadStatus,
) {
    companion object {
        fun create(
            uploadFileDirectoryFeatureName: UploadFileDirectoryFeatureName,
            contentType: ContentType,
        ): UploadFile {
            return UploadFile(
                id = UploadFileId(0L), // ID will be set by the repository
                fileKey = FileKey.from(uploadFileDirectoryFeatureName, contentType),
                status = UploadStatus.PENDING
            )
        }

        fun create(
            fileKey: FileKey,
        ): UploadFile {
            return UploadFile(
                id = UploadFileId(0L), // ID will be set by the repository
                fileKey = fileKey,
                status = UploadStatus.PENDING
            )
        }
    }

    fun upload() = UploadFile(
        id = id,
        fileKey = fileKey,
        status = UploadStatus.UPLOADED
    )
}

enum class UploadStatus {
    PENDING,
    UPLOADED,
}

enum class UploadFileDirectoryFeatureName {
    WORK_TEMPLATE,
    WORK_PLAN,
    WORK_TICKET,
    REAL_ESTATE,
    REPORT,
}

enum class ContentType(val extension: String) {
    PDF("pdf"),
    PNG("png"),
    JPEG("jpeg"),
    JPG("jpg"),
    CSV("csv");

    companion object {
        fun from(extension: String): ContentType {
            return entries.firstOrNull { it.extension == extension }
                ?: throw IllegalArgumentException("Unsupported file extension: $extension")
        }
    }
}

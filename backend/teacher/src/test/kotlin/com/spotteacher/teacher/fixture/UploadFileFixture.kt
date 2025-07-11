package com.spotteacher.teacher.fixture

import com.spotteacher.teacher.feature.uploadFile.domain.ContentType
import com.spotteacher.teacher.feature.uploadFile.domain.FileKey
import com.spotteacher.teacher.feature.uploadFile.domain.UploadFile
import com.spotteacher.teacher.feature.uploadFile.domain.UploadFileDirectoryFeatureName
import com.spotteacher.teacher.feature.uploadFile.domain.UploadFileId
import com.spotteacher.teacher.feature.uploadFile.domain.UploadStatus
import org.springframework.stereotype.Component

@Component
class UploadFileFixture {

    private var uploadFileIdCount = 1L

    fun buildUploadFile(
        id: UploadFileId = UploadFileId(uploadFileIdCount++),
        fileKey: FileKey = FileKey("test-key-${uploadFileIdCount}.pdf"),
        status: UploadStatus = UploadStatus.UPLOADED
    ): UploadFile {
        return UploadFile(
            id = id,
            fileKey = fileKey,
            status = status
        )
    }

    fun buildPendingUploadFile(
        directoryFeatureName: UploadFileDirectoryFeatureName = UploadFileDirectoryFeatureName.REPORT,
        contentType: ContentType = ContentType.PDF
    ): UploadFile {
        return UploadFile.create(
            uploadFileDirectoryFeatureName = directoryFeatureName,
            contentType = contentType
        )
    }
}
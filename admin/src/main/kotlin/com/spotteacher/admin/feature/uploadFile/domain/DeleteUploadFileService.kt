package com.spotteacher.admin.feature.uploadFile.domain

import arrow.core.Nel
import com.spotteacher.admin.shared.infra.TransactionCoroutine
import org.springframework.stereotype.Service

@Service
class DeleteUploadFileService(
    private val repository: UploadFileRepository,
) {
    @TransactionCoroutine
    suspend fun delete(
        ids: Nel<UploadFileId>,
    ) {
        repository.bulkDelete(ids)
    }
}

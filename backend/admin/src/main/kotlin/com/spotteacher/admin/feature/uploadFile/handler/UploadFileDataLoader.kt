package com.spotteacher.admin.feature.uploadFile.handler

import arrow.core.Nel
import com.spotteacher.admin.feature.uploadFile.domain.UploadFileId
import com.spotteacher.admin.feature.uploadFile.domain.UploadFileRepository
import com.spotteacher.admin.shared.graphql.KotlinCoroutineDataLoader
import graphql.schema.DataFetchingEnvironment
import org.dataloader.DataLoader
import org.springframework.stereotype.Component

private const val DATA_LOADER_NAME = "UploadFileDataLoader"

@Component
class UploadFileDataLoader(private val repository: UploadFileRepository) :
    KotlinCoroutineDataLoader<UploadFileId, UploadFileType?> {
    override val dataLoaderName = DATA_LOADER_NAME

    override suspend fun batchLoad(keys: Nel<UploadFileId>): List<UploadFileType?> {
        val uploadFiles = repository.filterByIds(keys).associateBy { it.id }

        return keys.map { uploadFileId ->
            val uploadFile = uploadFiles[uploadFileId] ?: return@map null
            UploadFileType(
                id = uploadFile.id.toGraphQLId(),
                fileKey = uploadFile.fileKey.value,
                uploadStatus = uploadFile.status
            )
        }
    }
}

fun DataFetchingEnvironment.getUploadFileDataLoader(): DataLoader<UploadFileId, UploadFileType?> {
    return this.getDataLoader<UploadFileId, UploadFileType?>(DATA_LOADER_NAME)!!
}

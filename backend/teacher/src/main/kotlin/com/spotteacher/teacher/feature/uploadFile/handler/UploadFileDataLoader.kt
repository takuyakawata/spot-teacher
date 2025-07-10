package com.spotteacher.teacher.feature.uploadFile.handler

// import com.spotteacher.admin.feature.uploadFile.domain.UploadFileId

// private const val DATA_LOADER_NAME = "UploadFileDataLoader"

// data class UploadFileDataLoaderKey(
//    val uploadFileId: UploadFileId,
// )

// @Component
// class UploadFileDataLoader(private val repository: UploadFileRepository) :
//    KotlinCoroutineDataLoader<UploadFileDataLoaderKey, UploadFileType?> {
//    override val dataLoaderName = DATA_LOADER_NAME
//
//    override suspend fun batchLoad(keys: Nel<UploadFileDataLoaderKey>): List<UploadFileType?> {
//        // For each tenant, fetch the upload files and map them to UploadFileType
//        val uploadFiles = repository.filterByIds(uploadFileIds).associateBy { it.id }
//
//        return keysByTenant.flatMap { (tenantId, keysForTenant) ->
//            val uploadFileIds = keysForTenant.map { it.uploadFileId }.toNonEmptyListOrNull() ?: return@flatMap emptyList()
//
//
//            // Map domain objects to GraphQL types
//            keysForTenant.map { (uploadFileId, _) ->
//                val uploadFile = uploadFiles[uploadFileId] ?: return@map null
//                UploadFileType(
//                    id = uploadFile.id.toGraphQLId(),
//                    fileKey = uploadFile.fileKey.value,
//                    uploadStatus = uploadFile.status
//                )
//            }
//        }
//    }
// }
//
// fun DataFetchingEnvironment.getUploadFileDataLoader(): DataLoader<UploadFileDataLoaderKey, UploadFileType> {
//    return this.getDataLoader<UploadFileDataLoaderKey, UploadFileType>(DATA_LOADER_NAME)!!
// }

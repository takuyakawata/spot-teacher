

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

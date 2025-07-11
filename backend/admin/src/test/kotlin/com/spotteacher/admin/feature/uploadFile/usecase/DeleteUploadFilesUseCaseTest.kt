package com.spotteacher.admin.feature.uploadFile.usecase

import arrow.core.nonEmptyListOf
import arrow.core.toNonEmptyListOrNull
import com.spotteacher.admin.feature.uploadFile.domain.DeleteUploadFileService
import com.spotteacher.admin.feature.uploadFile.domain.FileKey
import com.spotteacher.admin.feature.uploadFile.domain.UploadFile
import com.spotteacher.admin.feature.uploadFile.domain.UploadFileId
import com.spotteacher.admin.feature.uploadFile.domain.UploadFileRepository
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk

class DeleteUploadFilesUseCaseTest : DescribeSpec({

    val deleteUploadFileService = mockk<DeleteUploadFileService>()
    val uploadFileRepository = mockk<UploadFileRepository>()
    val useCase = DeleteUploadFilesUseCase(deleteUploadFileService, uploadFileRepository)

    describe("call") {
        context("when deleting multiple upload files") {
            val fileIds = nonEmptyListOf(
                UploadFileId(1),
                UploadFileId(2),
                UploadFileId(3)
            )

            val mockFiles = listOf(
                mockk<UploadFile>().apply {
                    coEvery { fileKey } returns FileKey("file1")
                },
                mockk<UploadFile>().apply {
                    coEvery { fileKey } returns FileKey("file2")
                },
                mockk<UploadFile>().apply {
                    coEvery { fileKey } returns FileKey("file3")
                }
            )

            it("should call service.delete and repository methods with the correct ids") {
                coEvery { deleteUploadFileService.delete(fileIds) } returns Unit
                coEvery { uploadFileRepository.filterByIds(fileIds) } returns mockFiles
                coEvery { uploadFileRepository.deleteFile(any()) } returns Unit

                useCase.call(fileIds)

                coVerify(exactly = 1) {
                    deleteUploadFileService.delete(fileIds)
                    uploadFileRepository.filterByIds(fileIds)
                }

                coVerify(exactly = 3) {
                    uploadFileRepository.deleteFile(any())
                }
            }
        }
    }
})

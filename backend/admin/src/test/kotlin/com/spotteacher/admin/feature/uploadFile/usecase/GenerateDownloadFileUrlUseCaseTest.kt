package com.spotteacher.admin.feature.uploadFile.usecase

import com.spotteacher.admin.feature.uploadFile.domain.FileKey
import com.spotteacher.admin.feature.uploadFile.domain.UploadFile
import com.spotteacher.admin.feature.uploadFile.domain.UploadFileId
import com.spotteacher.admin.feature.uploadFile.domain.UploadFileRepository
import com.spotteacher.admin.feature.uploadFile.domain.UploadStatus
import com.spotteacher.exception.ResourceNotFoundException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk

class GenerateDownloadFileUrlUseCaseTest : DescribeSpec({
    val uploadFileRepository = mockk<UploadFileRepository>()
    val useCase = GenerateDownloadFileUrlUseCase(uploadFileRepository)

    describe("call") {
        val uploadFileId = UploadFileId(1)
        val fileKey = FileKey("test/file.jpg")
        val input = GenerateDownloadFileUrlUseCaseInput(
            uploadFileId = uploadFileId
        )

        context("when file exists") {
            it("should return generated download URL") {
                val uploadFile = UploadFile(
                    id = uploadFileId,
                    fileKey = fileKey,
                    status = UploadStatus.UPLOADED
                )

                coEvery {
                    uploadFileRepository.findById(uploadFileId)
                } returns uploadFile

                coEvery {
                    uploadFileRepository.generateDownloadUrl(fileKey)
                } returns "https://example.com/download/test/file.jpg"

                val result = useCase.call(input)
                result.url shouldBe "https://example.com/download/test/file.jpg"
            }
        }

        context("when file does not exist") {
            it("should throw ResourceNotFoundException") {
                coEvery {
                    uploadFileRepository.findById(uploadFileId)
                } returns null

                shouldThrow<ResourceNotFoundException> {
                    useCase.call(input)
                }
            }
        }
    }
})

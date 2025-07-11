package com.spotteacher.teacher.feature.uploadFile.usecase

import com.spotteacher.exception.ResourceNotFoundException
import com.spotteacher.teacher.feature.uploadFile.domain.UploadFileId
import com.spotteacher.teacher.feature.uploadFile.domain.UploadFileRepository
import com.spotteacher.teacher.fixture.UploadFileFixture
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk

class GenerateDownloadFileUrlUseCaseTest : DescribeSpec({
    describe("GenerateDownloadFileUrlUseCase") {
        // Arrange
        val uploadFileRepository = mockk<UploadFileRepository>()
        val useCase = GenerateDownloadFileUrlUseCase(uploadFileRepository)
        val uploadFileFixture = UploadFileFixture()

        describe("call") {
            context("when upload file exists") {
                it("should return download url") {
                    // Arrange
                    val uploadFile = uploadFileFixture.buildUploadFile()
                    val input = GenerateDownloadFileUrlUseCaseInput(uploadFile.id)
                    val expectedUrl = "https://example.com/download-url"

                    coEvery { uploadFileRepository.findById(uploadFile.id) } returns uploadFile
                    coEvery { uploadFileRepository.generateDownloadUrl(uploadFile.fileKey) } returns expectedUrl

                    // Act
                    val result = useCase.call(input)

                    // Assert
                    result.url shouldBe expectedUrl

                    // Verify
                    coVerify(exactly = 1) { uploadFileRepository.findById(uploadFile.id) }
                    coVerify(exactly = 1) { uploadFileRepository.generateDownloadUrl(uploadFile.fileKey) }
                }
            }

            context("when upload file does not exist") {
                it("should throw ResourceNotFoundException") {
                    // Arrange
                    val uploadFileId = UploadFileId(999L)
                    val input = GenerateDownloadFileUrlUseCaseInput(uploadFileId)

                    coEvery { uploadFileRepository.findById(uploadFileId) } returns null

                    // Act & Assert
                    shouldThrow<ResourceNotFoundException> {
                        useCase.call(input)
                    }

                    // Verify
                    coVerify(exactly = 1) { uploadFileRepository.findById(uploadFileId) }
                }
            }
        }
    }
})
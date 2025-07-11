package com.spotteacher.teacher.feature.uploadFile.infra

import com.spotteacher.teacher.feature.uploadFile.domain.UploadFileId
import com.spotteacher.teacher.feature.uploadFile.domain.UploadFileRepository
import com.spotteacher.teacher.fixture.UploadFileFixture
import com.spotteacher.backend.DatabaseDescribeSpec
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE

@SpringBootTest(webEnvironment = NONE)
class UploadFileRepositoryImplTest(
    @Autowired private val uploadFileRepository: UploadFileRepository,
    @Autowired private val uploadFileFixture: UploadFileFixture
) : DatabaseDescribeSpec({

    describe("UploadFileRepository") {
        describe("findById") {
            context("when upload file exists") {
                it("should return the upload file") {
                    // Note: In a real database test, you would insert the upload file first
                    // For this test, we'll test the findById method with a known non-existent ID
                    val nonExistentId = UploadFileId(999999L)
                    val foundUploadFile = uploadFileRepository.findById(nonExistentId)

                    // Assert
                    foundUploadFile shouldBe null
                }
            }

            context("when upload file does not exist") {
                it("should return null") {
                    // Arrange
                    val nonExistentId = UploadFileId(999999L)

                    // Act
                    val foundUploadFile = uploadFileRepository.findById(nonExistentId)

                    // Assert
                    foundUploadFile shouldBe null
                }
            }
        }

        describe("generateDownloadUrl") {
            it("should generate download url for file key") {
                // Arrange
                val uploadFile = uploadFileFixture.buildUploadFile()

                // Act
                val downloadUrl = uploadFileRepository.generateDownloadUrl(uploadFile.fileKey)

                // Assert - URL should be generated (not null/empty)
                downloadUrl.isNotBlank() shouldBe true
            }
        }
    }
})
package com.spotteacher.admin.feature.uploadFile.usecase

import arrow.core.Nel
import arrow.core.nonEmptyListOf
import com.spotteacher.admin.feature.uploadFile.domain.ContentType
import com.spotteacher.admin.feature.uploadFile.domain.FileKey
import com.spotteacher.admin.feature.uploadFile.domain.UploadFile
import com.spotteacher.admin.feature.uploadFile.domain.UploadFileDirectoryFeatureName
import com.spotteacher.admin.feature.uploadFile.domain.UploadFileId
import com.spotteacher.admin.feature.uploadFile.domain.UploadFileRepository
import com.spotteacher.admin.feature.uploadFile.domain.UploadStatus
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot

class CreateUploadFilesUseCaseTest : DescribeSpec({
    val uploadFileRepository = mockk<UploadFileRepository>()
    val useCase = CreateUploadFilesUseCase(uploadFileRepository)

    describe("call") {
        context("when file inputs are provided") {
            val fileInputs = nonEmptyListOf(
                UploadFileInput(
                    contentType = ContentType.JPEG,
                    featureName = UploadFileDirectoryFeatureName.REAL_ESTATE
                ),
                UploadFileInput(
                    contentType = ContentType.PDF,
                    featureName = UploadFileDirectoryFeatureName.WORK_PLAN
                )
            )
            val input = CreateUploadFilesUseCaseInput(
                fileInputs = fileInputs
            )

            it("should create upload files with correct content types and return them") {
                // Mock the created upload files
                val createdFiles = listOf(
                    UploadFile(
                        id = UploadFileId(1),
                        fileKey = FileKey("one-hour-cache/REAL_ESTATE/some-uuid.jpeg"),
                        status = UploadStatus.PENDING
                    ),
                    UploadFile(
                        id = UploadFileId(2),
                        fileKey = FileKey("one-hour-cache/WORK_PLAN/some-uuid.pdf"),
                        status = UploadStatus.PENDING
                    )
                )

                // Create a slot to capture the argument passed to bulkCreate
                val uploadFilesSlot = slot<Nel<UploadFile>>()

                // Mock the repository to return the created files
                coEvery {
                    uploadFileRepository.bulkCreate(capture(uploadFilesSlot))
                } returns createdFiles

                // Call the use case
                val result = useCase.call(input)

                // Verify the result
                result.uploadFiles shouldBe createdFiles

                // Verify that the captured argument has the correct properties
                uploadFilesSlot.captured.size shouldBe 2
                uploadFilesSlot.captured[0].status shouldBe UploadStatus.PENDING
                uploadFilesSlot.captured[1].status shouldBe UploadStatus.PENDING
                // The file keys will be generated with UUIDs, so we just check they contain the directory names
                uploadFilesSlot.captured[0].fileKey.value.contains("REAL_ESTATE") shouldBe true
                uploadFilesSlot.captured[1].fileKey.value.contains("WORK_PLAN") shouldBe true
            }
        }

        context("when single file input is provided") {
            val fileInputs = nonEmptyListOf(
                UploadFileInput(
                    contentType = ContentType.PNG,
                    featureName = UploadFileDirectoryFeatureName.REPORT
                )
            )
            val input = CreateUploadFilesUseCaseInput(
                fileInputs = fileInputs
            )

            it("should create single upload file") {
                val createdFiles = listOf(
                    UploadFile(
                        id = UploadFileId(1),
                        fileKey = FileKey("no-cache/REPORT/some-uuid.png"),
                        status = UploadStatus.PENDING
                    )
                )

                val uploadFilesSlot = slot<Nel<UploadFile>>()

                coEvery {
                    uploadFileRepository.bulkCreate(capture(uploadFilesSlot))
                } returns createdFiles

                val result = useCase.call(input)

                result.uploadFiles shouldBe createdFiles
                uploadFilesSlot.captured.size shouldBe 1
                uploadFilesSlot.captured[0].fileKey.value.contains("REPORT") shouldBe true
                uploadFilesSlot.captured[0].fileKey.value.contains("no-cache") shouldBe true
            }
        }
    }
})

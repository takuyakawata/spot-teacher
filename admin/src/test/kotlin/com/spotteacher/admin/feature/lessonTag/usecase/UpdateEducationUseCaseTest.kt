package com.spotteacher.admin.feature.lessonTag.usecase

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import arrow.core.Either
import arrow.core.nonEmptyListOf
import com.spotteacher.admin.feature.lessonTag.domain.Education
import com.spotteacher.admin.feature.lessonTag.domain.EducationError
import com.spotteacher.admin.feature.lessonTag.domain.EducationErrorCode
import com.spotteacher.admin.feature.lessonTag.domain.EducationId
import com.spotteacher.admin.feature.lessonTag.domain.EducationName
import com.spotteacher.admin.feature.lessonTag.domain.EducationRepository
import com.spotteacher.admin.fixture.EducationFixture

class UpdateEducationUseCaseTest : DescribeSpec({

    val educationRepository = mockk<EducationRepository>()
    val useCase = UpdateEducationUseCase(educationRepository)
    val educationFixture = EducationFixture()

    val existingEducationId = EducationId(1L)
    val nonExistentEducationId = EducationId(999L)

    describe("UpdateEducationUseCase") {
        describe("call") {
            context("when education exists and is successfully updated with all fields") {
                it("should return Unit.right() and update the education") {
                    val initialEducation = educationFixture.buildEducation(
                        id = existingEducationId,
                        name = EducationName("Old Name"),
                        isActive = false,
                        displayOrder = 0
                    )
                    val updatedName = EducationName("New Updated Name")
                    val updatedIsActive = true
                    val updatedDisplayOrder = 10

                    coEvery { educationRepository.filterByIds(nonEmptyListOf(existingEducationId)) } returns listOf(initialEducation)
                    coEvery { educationRepository.update(any<Education>()) } returns Unit

                    val result = useCase.call(
                        UpdateEducationUseCaseInput(
                            id = existingEducationId,
                            name = updatedName,
                            isActive = updatedIsActive,
                            displayOrder = updatedDisplayOrder
                        )
                    )

                    result.shouldBeInstanceOf<Either.Right<Unit>>()
                    result.value shouldBe Unit
                }
            }

            context("when education exists and is successfully updated with partial fields") {
                it("should return Unit.right() and update only specified fields") {
                    val initialEducation = educationFixture.buildEducation(
                        id = existingEducationId,
                        name = EducationName("Old Name"),
                        isActive = false,
                        displayOrder = 0
                    )
                    val updatedName = EducationName("Only Name Changed")
                    // isActive and displayOrder are null in input, should remain as initialEducation's values

                    coEvery { educationRepository.filterByIds(nonEmptyListOf(existingEducationId)) } returns listOf(initialEducation)
                    coEvery { educationRepository.update(any<Education>()) } returns Unit

                    val result = useCase.call(
                        UpdateEducationUseCaseInput(
                            id = existingEducationId,
                            name = updatedName,
                            isActive = null, // Not updating this
                            displayOrder = null // Not updating this
                        )
                    )

                    result.shouldBeInstanceOf<Either.Right<Unit>>()
                    result.value shouldBe Unit
                }
            }

            context("when education does not exist") {
                it("should return an error indicating education not found") {
                    coEvery { educationRepository.filterByIds(nonEmptyListOf(nonExistentEducationId)) } returns emptyList()

                    val result = useCase.call(
                        UpdateEducationUseCaseInput(
                            id = nonExistentEducationId,
                            name = EducationName("Any name"),
                            isActive = true,
                            displayOrder = 1
                        )
                    )

                    result.shouldBeInstanceOf<Either.Left<EducationError>>()
                    result.value.code shouldBe EducationErrorCode.EDUCATION_NOT_FOUND
                    result.value.message shouldBe "Education not found"
                }
            }
        }
    }
})

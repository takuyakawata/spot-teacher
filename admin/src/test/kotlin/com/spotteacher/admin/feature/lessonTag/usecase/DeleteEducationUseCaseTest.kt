package com.spotteacher.admin.feature.lessonTag.usecase

import arrow.core.Either
import arrow.core.nonEmptyListOf
import com.spotteacher.admin.feature.lessonTag.domain.EducationError
import com.spotteacher.admin.feature.lessonTag.domain.EducationErrorCode
import com.spotteacher.admin.feature.lessonTag.domain.EducationId
import com.spotteacher.admin.feature.lessonTag.domain.EducationRepository
import com.spotteacher.admin.fixture.EducationFixture
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.mockk

class DeleteEducationUseCaseTest : DescribeSpec({
    val educationRepository = mockk<EducationRepository>()
    val useCase = DeleteEducationUseCase(educationRepository)
    val educationFixture = EducationFixture()

    val existingEducationId = EducationId(1L)
    val nonExistentEducationId = EducationId(999L)

    describe("DeleteEducationUseCase") {
        describe("call") {
            context("when education exists and is successfully deleted") {
                it("should return Unit.right()") {
                    val existingEducation = educationFixture.buildEducation(id = existingEducationId)

                    coEvery { educationRepository.filterByIds(nonEmptyListOf(existingEducationId)) } returns listOf(
                        existingEducation
                    )
                    coEvery { educationRepository.delete(existingEducationId) } returns Unit

                    val result = useCase.call(id = existingEducationId)

                    result.shouldBeInstanceOf<Either.Right<Unit>>()
                    result.value shouldBe Unit
                }
            }

            context("when education does not exist") {
                it("should return an error indicating education not found") {
                    coEvery { educationRepository.filterByIds(nonEmptyListOf(nonExistentEducationId)) } returns emptyList()

                    val result = useCase.call(id = nonExistentEducationId)

                    result.shouldBeInstanceOf<Either.Left<EducationError>>()
                    result.value.code shouldBe EducationErrorCode.EDUCATION_NOT_FOUND
                    result.value.message shouldBe "Education not found"
                }
            }
        }
    }
})

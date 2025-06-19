package com.spotteacher.admin.feature.lessonPlan.usecase

import arrow.core.Either
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanError
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanErrorCode
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanId
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanRepository
import com.spotteacher.admin.fixture.LessonPlanFixture
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import io.mockk.coEvery
import io.mockk.mockk

class DeleteLessonPlanUseCaseTest : DescribeSpec({
    describe("DeleteLessonPlanUseCase") {
        // Arrange
        val lessonPlanRepository = mockk<LessonPlanRepository>()
        val fixture = LessonPlanFixture()
        val useCase = DeleteLessonPlanUseCase(lessonPlanRepository)

        // Test data
        val lessonPlanId = LessonPlanId(1)

        // Create a lesson plan using fixture
        val publishedLessonPlan = fixture.buildPublishedLessonPlan(
            id = lessonPlanId
        )

        describe("call") {
            context("when the lesson plan exists") {
                it("should delete the lesson plan and return success") {
                    // Arrange
                    coEvery { lessonPlanRepository.findById(lessonPlanId) } returns publishedLessonPlan
                    coEvery { lessonPlanRepository.delete(lessonPlanId) } returns Unit

                    // Act
                    val result = useCase.call(lessonPlanId)

                    // Assert
                    result shouldBe Either.Right(Unit)
                }
            }

            context("when the lesson plan does not exist") {
                it("should return LESSON_PLAN_NOT_FOUND error") {
                    // Arrange
                    coEvery { lessonPlanRepository.findById(lessonPlanId) } returns null

                    // Act
                    val result = useCase.call(lessonPlanId)

                    // Assert
                    result.shouldBeTypeOf<Either.Left<LessonPlanError>>()
                    val error = (result as Either.Left<LessonPlanError>).value
                    error.code shouldBe LessonPlanErrorCode.LESSON_PLAN_NOT_FOUND
                    error.message shouldBe "Lesson plan with ID ${lessonPlanId.value} not found"
                }
            }
        }
    }
})

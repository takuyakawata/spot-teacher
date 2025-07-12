package com.spotteacher.teacher.feature.lessonPlan.usecase

import com.spotteacher.teacher.feature.lessonPlan.domain.LessonPlan
import com.spotteacher.teacher.feature.lessonPlan.domain.LessonPlanErrorCode
import com.spotteacher.teacher.feature.lessonPlan.domain.LessonPlanId
import com.spotteacher.teacher.feature.lessonPlan.domain.LessonPlanRepository
import com.spotteacher.teacher.fixture.LessonPlanFixture
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk

class FindLessonPlanUseCaseTest : DescribeSpec({
    describe("FindLessonPlanUseCase") {
        // Arrange
        val lessonPlanRepository = mockk<LessonPlanRepository>()
        val lessonPlanFixture = LessonPlanFixture()
        val useCase = FindLessonPlanUseCase(lessonPlanRepository)

        // Test data
        val lessonPlanId = LessonPlanId(1)
        val lessonPlan = lessonPlanFixture.buildLessonPlan(id = lessonPlanId)

        describe("call") {
            it("should return success Result with LessonPlan when found") {
                // Arrange
                coEvery { lessonPlanRepository.findById(lessonPlanId) } returns lessonPlan

                // Act
                val result = useCase.call(lessonPlanId)

                // Assert
                result.isSuccess shouldBe true
                result.getOrNull() shouldBe lessonPlan

                coVerify { lessonPlanRepository.findById(lessonPlanId) }
            }

            it("should return failure Result with LESSON_PLAN_NOT_FOUND error when lesson plan not found") {
                // Arrange
                coEvery { lessonPlanRepository.findById(lessonPlanId) } returns null

                // Act
                val result = useCase.call(lessonPlanId)

                // Assert
                result.isFailure shouldBe true
                val exception = result.exceptionOrNull()
                exception.shouldBeTypeOf<RuntimeException>()
                exception!!.message!!.contains(LessonPlanErrorCode.LESSON_PLAN_NOT_FOUND.name) shouldBe true

                coVerify { lessonPlanRepository.findById(lessonPlanId) }
            }
        }
    }
})
package com.spotteacher.admin.feature.lessonPlan.usecase

import arrow.core.Either
import arrow.core.toNonEmptyListOrNull
import com.spotteacher.admin.feature.company.domain.CompanyId
import com.spotteacher.admin.feature.lessonPlan.domain.DraftLessonPlan
import com.spotteacher.admin.feature.lessonPlan.domain.LessonLocation
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanDate
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanDescription
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanError
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanErrorCode
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanId
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanRepository
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanTitle
import com.spotteacher.admin.feature.lessonPlan.domain.LessonType
import com.spotteacher.admin.feature.lessonPlan.domain.PublishedLessonPlan
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import java.time.LocalDateTime
import java.time.LocalTime

class FindLessonPlanUseCaseTest : DescribeSpec({
    describe("FindLessonPlanUseCase") {
        // Arrange
        val lessonPlanRepository = mockk<LessonPlanRepository>()
        val useCase = FindLessonPlanUseCase(lessonPlanRepository)

        // Test data
        val lessonPlanId = LessonPlanId(1)
        val companyId = CompanyId(1)
        val createdAt = LocalDateTime.now()

        // Create a LessonPlanDate for testing
        val lessonPlanDate = LessonPlanDate(
            startMonth = 1,
            startDay = 1,
            endMonth = 12,
            endDay = 31,
            startTime = LocalTime.of(9, 0),
            endTime = LocalTime.of(17, 0)
        )
        val lessonPlanDates = listOf(lessonPlanDate).toNonEmptyListOrNull()!!

        describe("call") {
            it("should return Right with PublishedLessonPlan when found") {
                // Arrange
                val publishedLessonPlan = PublishedLessonPlan(
                    id = lessonPlanId,
                    companyId = companyId,
                    images = emptyList(),
                    createdAt = createdAt,
                    title = LessonPlanTitle("Test Lesson Plan"),
                    description = LessonPlanDescription("This is a test lesson plan description"),
                    lessonType = LessonType.ONLINE,
                    location = LessonLocation("Test Location"),
                    annualMaxExecutions = 10,
                    lessonPlanDates = lessonPlanDates
                )

                coEvery { lessonPlanRepository.findById(lessonPlanId) } returns publishedLessonPlan

                // Act
                val result = useCase.call(FindLessonPlanUseCaseInput(lessonPlanId))

                // Assert
                result.shouldBeTypeOf<Either.Right<PublishedLessonPlan>>()
                result.value shouldBe publishedLessonPlan

                coVerify { lessonPlanRepository.findById(lessonPlanId) }
            }

            it("should return Right with DraftLessonPlan when found") {
                // Arrange
                val draftLessonPlan = DraftLessonPlan(
                    id = lessonPlanId,
                    companyId = companyId,
                    images = emptyList(),
                    createdAt = createdAt,
                    title = LessonPlanTitle("Draft Lesson Plan"),
                    description = LessonPlanDescription("This is a draft lesson plan description"),
                    lessonType = LessonType.OFFLINE,
                    location = LessonLocation("Draft Location"),
                    annualMaxExecutions = 5,
                    lessonPlanDates = lessonPlanDates
                )

                coEvery { lessonPlanRepository.findById(lessonPlanId) } returns draftLessonPlan

                // Act
                val result = useCase.call(FindLessonPlanUseCaseInput(lessonPlanId))

                // Assert
                result.shouldBeTypeOf<Either.Right<DraftLessonPlan>>()
                result.value shouldBe draftLessonPlan

                coVerify { lessonPlanRepository.findById(lessonPlanId) }
            }

            it("should return Left with LESSON_PLAN_NOT_FOUND error when lesson plan not found") {
                // Arrange
                coEvery { lessonPlanRepository.findById(lessonPlanId) } returns null

                // Act
                val result = useCase.call(FindLessonPlanUseCaseInput(lessonPlanId))

                // Assert
                result.shouldBeTypeOf<Either.Left<LessonPlanError>>()
                val error = (result as Either.Left<LessonPlanError>).value
                error.code shouldBe LessonPlanErrorCode.LESSON_PLAN_NOT_FOUND
                error.message shouldBe "Lesson plan not found"

                coVerify { lessonPlanRepository.findById(lessonPlanId) }
            }
        }
    }
})

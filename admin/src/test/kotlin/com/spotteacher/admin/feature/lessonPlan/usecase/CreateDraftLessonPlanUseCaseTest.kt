package com.spotteacher.admin.feature.lessonPlan.usecase

import arrow.core.toNonEmptyListOrNull
import com.spotteacher.admin.feature.company.domain.CompanyId
import com.spotteacher.admin.feature.lessonPlan.domain.DraftLessonPlan
import com.spotteacher.admin.feature.lessonPlan.domain.LessonLocation
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanDate
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanDescription
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanRepository
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanTitle
import com.spotteacher.admin.feature.lessonPlan.domain.LessonType
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import java.time.LocalTime

class CreateDraftLessonPlanUseCaseTest : DescribeSpec({
    describe("CreateDraftLessonPlanUseCase") {
        // Arrange
        val lessonPlanRepository = mockk<LessonPlanRepository>()
        val useCase = CreateDraftLessonPlanUseCase(lessonPlanRepository)

        // Test data
        val companyId = CompanyId(1)
        val title = LessonPlanTitle("Test Lesson Plan")
        val description = LessonPlanDescription("This is a test lesson plan description")
        val lessonType = LessonType.ONLINE
        val location = LessonLocation("Test Location")
        val annualMaxExecutions = 10
        val lessonPlanDate = LessonPlanDate(
            startMonth = 1,
            startDay = 1,
            endMonth = 12,
            endDay = 31,
            startTime = LocalTime.of(9, 0),
            endTime = LocalTime.of(17, 0)
        )
        val lessonPlanDates = listOf(lessonPlanDate).toNonEmptyListOrNull()

        describe("call") {
            it("should create a draft lesson plan") {
                // Arrange
                val draftLessonPlan = DraftLessonPlan.create(
                    companyId = companyId,
                    title = title,
                    description = description,
                    lessonType = lessonType,
                    location = location,
                    annualMaxExecutions = annualMaxExecutions,
                    lessonPlanDates = lessonPlanDates
                )
                
                coEvery { lessonPlanRepository.createDraft(any()) } returns draftLessonPlan

                // Act
                useCase.call(
                    CreateDraftLessonPlanUseCaseInput(
                        companyId = companyId,
                        title = title,
                        description = description,
                        publish = false,
                        lessonType = lessonType,
                        location = location,
                        annualMaxExecutions = annualMaxExecutions,
                        lessonPlanDates = lessonPlanDates?.toList()
                    )
                )

                // Assert
                coVerify { 
                    lessonPlanRepository.createDraft(match { 
                        it.companyId == companyId &&
                        it.title == title &&
                        it.description == description &&
                        it.lessonType == lessonType &&
                        it.location == location &&
                        it.annualMaxExecutions == annualMaxExecutions &&
                        it.lessonPlanDates == lessonPlanDates
                    })
                }
            }

            it("should create a draft lesson plan with minimal required fields") {
                // Arrange
                val minimalDraftLessonPlan = DraftLessonPlan.create(
                    companyId = companyId,
                    title = null,
                    description = null,
                    lessonType = null,
                    location = null,
                    annualMaxExecutions = null,
                    lessonPlanDates = null
                )
                
                coEvery { lessonPlanRepository.createDraft(any()) } returns minimalDraftLessonPlan

                // Act
                useCase.call(
                    CreateDraftLessonPlanUseCaseInput(
                        companyId = companyId,
                        title = null,
                        description = null,
                        publish = false,
                        lessonType = null,
                        location = null,
                        annualMaxExecutions = null,
                        lessonPlanDates = null
                    )
                )

                // Assert
                coVerify { 
                    lessonPlanRepository.createDraft(match { 
                        it.companyId == companyId &&
                        it.title == null &&
                        it.description == null &&
                        it.lessonType == null &&
                        it.location == null &&
                        it.annualMaxExecutions == null &&
                        it.lessonPlanDates == null
                    })
                }
            }
        }
    }
})

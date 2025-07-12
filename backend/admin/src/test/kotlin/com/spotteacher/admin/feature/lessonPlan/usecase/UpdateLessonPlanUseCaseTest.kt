package com.spotteacher.admin.feature.lessonPlan.usecase

import arrow.core.Either
import com.spotteacher.admin.feature.lessonPlan.domain.DraftLessonPlan
import com.spotteacher.admin.feature.lessonPlan.domain.LessonLocation
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanDescription
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanEducations
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanError
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanErrorCode
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanGrades
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanId
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanRepository
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanSubjects
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanTitle
import com.spotteacher.admin.feature.lessonPlan.domain.LessonType
import com.spotteacher.admin.feature.lessonPlan.domain.PublishedLessonPlan
import com.spotteacher.admin.feature.lessonPlan.domain.update
import com.spotteacher.admin.feature.lessonTag.domain.EducationId
import com.spotteacher.admin.feature.lessonTag.domain.Grade
import com.spotteacher.admin.feature.lessonTag.domain.Subject
import com.spotteacher.admin.feature.uploadFile.domain.UploadFileId
import com.spotteacher.admin.fixture.CompanyFixture
import com.spotteacher.admin.fixture.LessonPlanFixture
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot

class UpdateLessonPlanUseCaseTest : DescribeSpec({
    describe("UpdateLessonPlanUseCase") {
        // Arrange
        val lessonPlanRepository = mockk<LessonPlanRepository>()
        val fixture = LessonPlanFixture()
        val useCase = UpdateLessonPlanUseCase(lessonPlanRepository)
        val companyFixture = CompanyFixture()

        // Test data
        val company = companyFixture.buildCompany()

        // Create lesson plans using fixture
        val originalPublishedLessonPlan = fixture.buildPublishedLessonPlan(
            companyId = company.id,
            title = LessonPlanTitle("Original Published Lesson Plan"),
            description = LessonPlanDescription("This is the original published lesson plan description"),
            lessonType = LessonType.ONLINE,
            location = LessonLocation("Original Location"),
            annualMaxExecutions = 10
        )

        val originalDraftLessonPlan = fixture.buildDraftLessonPlan(
            companyId = company.id,
            title = LessonPlanTitle("Original Draft Lesson Plan"),
            description = LessonPlanDescription("This is the original draft lesson plan description"),
            lessonType = LessonType.OFFLINE,
            location = LessonLocation("Original Draft Location"),
            annualMaxExecutions = 5
        )

        // Updated values
        val updatedTitle = LessonPlanTitle("Updated Lesson Plan")
        val updatedDescription = LessonPlanDescription("Updated lesson plan description")
        val updatedLessonType = LessonType.OFFLINE
        val updatedLocation = LessonLocation("Updated Location")
        val updatedAnnualMaxExecutions = 15
        val updatedImages = listOf(UploadFileId(1), UploadFileId(2))

        val educations = LessonPlanEducations(
            setOf(
                EducationId(1),
            )
        )

        val subjects = LessonPlanSubjects(
            setOf(
                Subject.SOCIAL_STUDIES,
            )
        )

        val grades = LessonPlanGrades(
            setOf(
                Grade.ELEMENTARY_1
            )
        )

        describe("call") {
            context("when updating a PublishedLessonPlan") {
                context("when all specified fields") {
                    it("should update all specified fields and return success") {
                        // Arrange
                        coEvery { lessonPlanRepository.findById(originalPublishedLessonPlan.id) } returns originalPublishedLessonPlan

                        val updatedOriginalPublishedLessonPlan = originalPublishedLessonPlan.update(
                            title = updatedTitle,
                            description = updatedDescription,
                            lessonType = updatedLessonType,
                            location = updatedLocation,
                            annualMaxExecutions = updatedAnnualMaxExecutions,
                            images = updatedImages,
                            educations = educations,
                            subjects = subjects,
                            grades = grades
                        )

                        coEvery { lessonPlanRepository.update(updatedOriginalPublishedLessonPlan) } returns Unit

                        // Act
                        val result = useCase.call(
                            UpdateLessonPlanUseCaseInput(
                                id = originalDraftLessonPlan.id,
                                title = updatedTitle,
                                description = updatedDescription,
                                lessonType = updatedLessonType,
                                location = updatedLocation,
                                annualMaxExecutions = updatedAnnualMaxExecutions,
                                images = updatedImages,
                                educations = educations,
                                subjects = subjects,
                                grades = grades
                            )
                        )

                        // Assert
                        result shouldBe Either.Right(Unit)
                    }
                }

                context("when only specified fields") {
                    it("should update keep others unchanged") {
                        // Arrange
                        coEvery { lessonPlanRepository.findById(originalPublishedLessonPlan.id) } returns originalPublishedLessonPlan

                        val updatedOriginalPublishedLessonPlan = originalPublishedLessonPlan.update(
                            title = updatedTitle,
                            description = updatedDescription,
                            lessonType = null,
                            location = null,
                            annualMaxExecutions = null,
                            images = null,
                            educations = null,
                            subjects = null,
                            grades = null
                        )

                        coEvery { lessonPlanRepository.update(updatedOriginalPublishedLessonPlan) } returns Unit

                        val lessonPlanSlot = slot<PublishedLessonPlan>()

                        // Act - only update title and description
                        val result = useCase.call(
                            UpdateLessonPlanUseCaseInput(
                                id = originalDraftLessonPlan.id,
                                title = updatedTitle,
                                description = updatedDescription,
                                lessonType = null,
                                location = null,
                                annualMaxExecutions = null,
                                images = null,
                                educations = educations,
                                subjects = subjects,
                                grades = grades
                            )
                        )

                        // Assert
                        result shouldBe Either.Right(Unit)
                    }
                }
            }

            context("when updating a DraftLessonPlan") {
                context("when all specified fields") {
                    it("should update all specified fields and return success") {
                        // Arrange
                        coEvery { lessonPlanRepository.findById(originalDraftLessonPlan.id) } returns originalDraftLessonPlan

                        val updatedOriginalDraftLessonPlan = originalDraftLessonPlan.update(
                            title = updatedTitle,
                            description = updatedDescription,
                            lessonType = updatedLessonType,
                            location = updatedLocation,
                            annualMaxExecutions = updatedAnnualMaxExecutions,
                            images = updatedImages,
                            educations = educations,
                            subjects = subjects,
                            grades = grades
                        )

                        coEvery { lessonPlanRepository.update(updatedOriginalDraftLessonPlan) } returns Unit

                        val lessonPlanSlot = slot<DraftLessonPlan>()

                        // Act
                        val result = useCase.call(
                            UpdateLessonPlanUseCaseInput(
                                id = originalDraftLessonPlan.id,
                                title = updatedTitle,
                                description = updatedDescription,
                                lessonType = updatedLessonType,
                                location = updatedLocation,
                                annualMaxExecutions = updatedAnnualMaxExecutions,
                                images = updatedImages,
                                educations = educations,
                                subjects = subjects,
                                grades = grades
                            )
                        )

                        // Assert
                        result shouldBe Either.Right(Unit)
                    }
                }

                context("when only specified fields") {
                    it("should update only specified fields and keep others unchanged") {
                        // Arrange
                        coEvery { lessonPlanRepository.findById(originalDraftLessonPlan.id) } returns originalDraftLessonPlan

                        val updatedOriginalDraftLessonPlan = originalDraftLessonPlan.update(
                            title = null,
                            description = null,
                            lessonType = null,
                            location = updatedLocation,
                            annualMaxExecutions = updatedAnnualMaxExecutions,
                            images = null,
                            educations = null,
                            subjects = null,
                            grades = null
                        )
                        coEvery { lessonPlanRepository.update(updatedOriginalDraftLessonPlan) } returns Unit

                        val lessonPlanSlot = slot<DraftLessonPlan>()

                        // Act - only update location and annualMaxExecutions
                        val result = useCase.call(
                            UpdateLessonPlanUseCaseInput(
                                id = originalDraftLessonPlan.id,
                                title = null,
                                description = null,
                                lessonType = null,
                                location = updatedLocation,
                                annualMaxExecutions = updatedAnnualMaxExecutions,
                                images = null,
                                educations = educations,
                                subjects = subjects,
                                grades = grades
                            )
                        )

                        // Assert
                        result shouldBe Either.Right(Unit)
                    }
                }

                context("when lesson plan does not exist") {
                    it("should return LESSON_PLAN_NOT_FOUND error") {
                        // Arrange
                        coEvery { lessonPlanRepository.findById(originalPublishedLessonPlan.id) } returns null

                        // Act
                        val result = useCase.call(
                            UpdateLessonPlanUseCaseInput(
                                id = originalPublishedLessonPlan.id,
                                title = updatedTitle,
                                description = updatedDescription,
                                lessonType = updatedLessonType,
                                location = updatedLocation,
                                annualMaxExecutions = updatedAnnualMaxExecutions,
                                images = updatedImages,
                                educations = educations,
                                subjects = subjects,
                                grades = grades
                            )
                        )

                        // Assert
                        result.shouldBeTypeOf<Either.Left<LessonPlanError>>()
                        val error = (result as Either.Left<LessonPlanError>).value
                        error.code shouldBe LessonPlanErrorCode.LESSON_PLAN_NOT_FOUND
                        error.message shouldBe "Lesson plan with ID ${originalPublishedLessonPlan.id.value} not found"
                    }
                }
            }
        }
    }
})

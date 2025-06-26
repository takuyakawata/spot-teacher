package com.spotteacher.admin.feature.lessonPlan.infra

import com.spotteacher.admin.feature.company.domain.CompanyId
import com.spotteacher.admin.feature.lessonPlan.domain.DraftLessonPlan
import com.spotteacher.admin.feature.lessonPlan.domain.LessonLocation
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanDescription
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanRepository
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanTitle
import com.spotteacher.admin.feature.lessonPlan.domain.LessonType
import com.spotteacher.admin.feature.lessonPlan.domain.PublishedLessonPlan
import com.spotteacher.admin.fixture.LessonPlanFixture
import com.spotteacher.backend.DatabaseDescribeSpec
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE

@SpringBootTest(webEnvironment = NONE)
class LessonPlanRepositoryImplTest(
    private val lessonPlanRepository: LessonPlanRepository,
    private val lessonPlanFixture: LessonPlanFixture
) : DatabaseDescribeSpec({

    describe("LessonPlanRepository") {

        describe("createDraft and findById") {
            it("should create a DraftLessonPlan and find it by ID") {
                // Create a new DraftLessonPlan
                val draftLessonPlan = lessonPlanFixture.buildDraftLessonPlan(
                    companyId = CompanyId(1),
                    title = LessonPlanTitle("Test Draft Lesson Plan"),
                    description = LessonPlanDescription("Test Description"),
                    lessonType = LessonType.ONLINE,
                    location = LessonLocation("Test Location"),
                    annualMaxExecutions = 5
                )

                // Create the lesson plan in the repository
                val createdLessonPlan = lessonPlanRepository.createDraft(draftLessonPlan)

                // Find the lesson plan by ID
                val foundLessonPlan = lessonPlanRepository.findById(createdLessonPlan.id)

                // Verify the lesson plan was found and has the correct properties
                foundLessonPlan shouldNotBe null
                foundLessonPlan as DraftLessonPlan
                foundLessonPlan.title?.value shouldBe "Test Draft Lesson Plan"
                foundLessonPlan.description?.value shouldBe "Test Description"
                foundLessonPlan.lessonType shouldBe LessonType.ONLINE
                foundLessonPlan.location?.value shouldBe "Test Location"
                foundLessonPlan.annualMaxExecutions shouldBe 5
            }
        }

        describe("update") {
            it("should update a DraftLessonPlan") {
                // Create a new DraftLessonPlan
                val draftLessonPlan = lessonPlanFixture.createDraftLessonPlan(
                    title = LessonPlanTitle("Original Title"),
                    description = LessonPlanDescription("Original Description"),
                    lessonType = LessonType.ONLINE,
                    location = LessonLocation("Original Location"),
                    annualMaxExecutions = 10
                )

                // Update the lesson plan
                val updatedLessonPlan = draftLessonPlan.copy(
                    title = LessonPlanTitle("Updated Title"),
                    description = LessonPlanDescription("Updated Description"),
                    lessonType = LessonType.OFFLINE,
                    location = LessonLocation("Updated Location"),
                    annualMaxExecutions = 15
                )
                lessonPlanRepository.update(updatedLessonPlan)

                // Find the lesson plan by ID
                val foundLessonPlan = lessonPlanRepository.findById(draftLessonPlan.id)

                // Verify the lesson plan was updated
                foundLessonPlan shouldNotBe null
                foundLessonPlan as DraftLessonPlan
                foundLessonPlan.title?.value shouldBe "Updated Title"
                foundLessonPlan.description?.value shouldBe "Updated Description"
                foundLessonPlan.lessonType shouldBe LessonType.OFFLINE
                foundLessonPlan.location?.value shouldBe "Updated Location"
                foundLessonPlan.annualMaxExecutions shouldBe 15
            }

            it("should update a PublishedLessonPlan") {
                // Create a new DraftLessonPlan and publish it
                val draftLessonPlan = lessonPlanFixture.createDraftLessonPlan(
                    title = LessonPlanTitle("Draft to Publish"),
                    description = LessonPlanDescription("Original Description"),
                    lessonType = LessonType.ONLINE,
                    location = LessonLocation("Original Location"),
                    annualMaxExecutions = 10
                )

                // Convert to PublishedLessonPlan
                val publishedLessonPlan = PublishedLessonPlan(
                    id = draftLessonPlan.id,
                    companyId = draftLessonPlan.companyId,
                    images = emptyList(),
                    createdAt = draftLessonPlan.createdAt,
                    title = draftLessonPlan.title!!,
                    description = draftLessonPlan.description!!,
                    lessonType = draftLessonPlan.lessonType!!,
                    location = draftLessonPlan.location!!,
                    annualMaxExecutions = draftLessonPlan.annualMaxExecutions!!,
                    lessonPlanDates = draftLessonPlan.lessonPlanDates!!,
                    educations = draftLessonPlan.educations,
                    subjects = draftLessonPlan.subjects,
                    grades = draftLessonPlan.grades,
                )

                // Update status to published
                lessonPlanRepository.updateStatus(publishedLessonPlan)

                // Update the published lesson plan
                val updatedPublishedLessonPlan = publishedLessonPlan.copy(
                    title = LessonPlanTitle("Updated Published Title"),
                    description = LessonPlanDescription("Updated Published Description"),
                    lessonType = LessonType.OFFLINE,
                    location = LessonLocation("Updated Published Location"),
                    annualMaxExecutions = 20
                )
                lessonPlanRepository.update(updatedPublishedLessonPlan)

                // Find the lesson plan by ID
                val foundLessonPlan = lessonPlanRepository.findById(publishedLessonPlan.id)

                // Verify the lesson plan was updated
                foundLessonPlan shouldNotBe null
                foundLessonPlan as PublishedLessonPlan
                foundLessonPlan.title.value shouldBe "Updated Published Title"
                foundLessonPlan.description.value shouldBe "Updated Published Description"
                foundLessonPlan.lessonType shouldBe LessonType.OFFLINE
                foundLessonPlan.location.value shouldBe "Updated Published Location"
                foundLessonPlan.annualMaxExecutions shouldBe 20
            }
        }

        describe("updateStatus") {
            it("should update a DraftLessonPlan to PublishedLessonPlan") {
                // Create a new DraftLessonPlan
                val draftLessonPlan = lessonPlanFixture.createDraftLessonPlan(
                    title = LessonPlanTitle("Draft to be Published"),
                    description = LessonPlanDescription("Draft Description"),
                    lessonType = LessonType.ONLINE,
                    location = LessonLocation("Draft Location"),
                    annualMaxExecutions = 10
                )

                // Convert to PublishedLessonPlan
                val publishedLessonPlan = PublishedLessonPlan(
                    id = draftLessonPlan.id,
                    companyId = draftLessonPlan.companyId,
                    images = emptyList(),
                    createdAt = draftLessonPlan.createdAt,
                    title = draftLessonPlan.title!!,
                    description = draftLessonPlan.description!!,
                    lessonType = draftLessonPlan.lessonType!!,
                    location = draftLessonPlan.location!!,
                    annualMaxExecutions = draftLessonPlan.annualMaxExecutions!!,
                    lessonPlanDates = draftLessonPlan.lessonPlanDates!!,
                    educations = draftLessonPlan.educations,
                    subjects = draftLessonPlan.subjects,
                    grades = draftLessonPlan.grades,
                )

                // Update status to published
                lessonPlanRepository.updateStatus(publishedLessonPlan)

                // Find the lesson plan by ID
                val foundLessonPlan = lessonPlanRepository.findById(draftLessonPlan.id)

                // Verify the lesson plan was updated to published
                foundLessonPlan shouldNotBe null
                foundLessonPlan as PublishedLessonPlan
                foundLessonPlan.title.value shouldBe "Draft to be Published"
                foundLessonPlan.description.value shouldBe "Draft Description"
                foundLessonPlan.lessonType shouldBe LessonType.ONLINE
                foundLessonPlan.location.value shouldBe "Draft Location"
                foundLessonPlan.annualMaxExecutions shouldBe 10
            }

            it("should update a PublishedLessonPlan to DraftLessonPlan") {
                // Create a new DraftLessonPlan and publish it
                val draftLessonPlan = lessonPlanFixture.createDraftLessonPlan(
                    title = LessonPlanTitle("Published to be Draft"),
                    description = LessonPlanDescription("Published Description"),
                    lessonType = LessonType.ONLINE,
                    location = LessonLocation("Published Location"),
                    annualMaxExecutions = 10
                )

                // Convert to PublishedLessonPlan
                val publishedLessonPlan = PublishedLessonPlan(
                    id = draftLessonPlan.id,
                    companyId = draftLessonPlan.companyId,
                    images = emptyList(),
                    createdAt = draftLessonPlan.createdAt,
                    title = draftLessonPlan.title!!,
                    description = draftLessonPlan.description!!,
                    lessonType = draftLessonPlan.lessonType!!,
                    location = draftLessonPlan.location!!,
                    annualMaxExecutions = draftLessonPlan.annualMaxExecutions!!,
                    lessonPlanDates = draftLessonPlan.lessonPlanDates!!,
                    educations = draftLessonPlan.educations,
                    subjects = draftLessonPlan.subjects,
                    grades = draftLessonPlan.grades,
                )

                // Update status to published
                lessonPlanRepository.updateStatus(publishedLessonPlan)

                // Convert back to DraftLessonPlan
                val backToDraftLessonPlan = DraftLessonPlan(
                    id = publishedLessonPlan.id,
                    companyId = publishedLessonPlan.companyId,
                    images = emptyList(),
                    createdAt = publishedLessonPlan.createdAt,
                    title = publishedLessonPlan.title,
                    description = publishedLessonPlan.description,
                    lessonType = publishedLessonPlan.lessonType,
                    location = publishedLessonPlan.location,
                    annualMaxExecutions = publishedLessonPlan.annualMaxExecutions,
                    lessonPlanDates = publishedLessonPlan.lessonPlanDates,
                    educations = draftLessonPlan.educations,
                    subjects = draftLessonPlan.subjects,
                    grades = draftLessonPlan.grades,
                )

                // Update status to draft
                lessonPlanRepository.updateStatus(backToDraftLessonPlan)

                // Find the lesson plan by ID
                val foundLessonPlan = lessonPlanRepository.findById(publishedLessonPlan.id)

                // Verify the lesson plan was updated to draft
                foundLessonPlan shouldNotBe null
                foundLessonPlan as DraftLessonPlan
                foundLessonPlan.title?.value shouldBe "Published to be Draft"
                foundLessonPlan.description?.value shouldBe "Published Description"
                foundLessonPlan.lessonType shouldBe LessonType.ONLINE
                foundLessonPlan.location?.value shouldBe "Published Location"
                foundLessonPlan.annualMaxExecutions shouldBe 10
            }
        }

        describe("delete") {
            it("should delete a lesson plan") {
                // Create a new DraftLessonPlan
                val draftLessonPlan = lessonPlanFixture.createDraftLessonPlan(
                    title = LessonPlanTitle("Lesson Plan to Delete"),
                    description = LessonPlanDescription("Delete Description"),
                    lessonType = LessonType.ONLINE,
                    location = LessonLocation("Delete Location"),
                    annualMaxExecutions = 10
                )

                // Verify the lesson plan exists
                val foundLessonPlan = lessonPlanRepository.findById(draftLessonPlan.id)
                foundLessonPlan shouldNotBe null

                // Delete the lesson plan
                lessonPlanRepository.delete(draftLessonPlan.id)

                // Verify the lesson plan no longer exists
                val deletedLessonPlan = lessonPlanRepository.findById(draftLessonPlan.id)
                deletedLessonPlan shouldBe null
            }
        }

        describe("getAll") {
            it("should return all lesson plans") {
                // Create multiple lesson plans
                val lessonPlan1 = lessonPlanFixture.createDraftLessonPlan(
                    title = LessonPlanTitle("Lesson Plan 1"),
                    description = LessonPlanDescription("Description 1"),
                    lessonType = LessonType.ONLINE,
                    location = LessonLocation("Location 1"),
                    annualMaxExecutions = 10
                )
                val lessonPlan2 = lessonPlanFixture.createDraftLessonPlan(
                    title = LessonPlanTitle("Lesson Plan 2"),
                    description = LessonPlanDescription("Description 2"),
                    lessonType = LessonType.OFFLINE,
                    location = LessonLocation("Location 2"),
                    annualMaxExecutions = 20
                )

                // Get all lesson plans
                val allLessonPlans = lessonPlanRepository.getAll()

                // Verify the lesson plans are returned
                allLessonPlans.shouldNotBeEmpty()

                // Find the created lesson plans in the list
                val foundLessonPlan1 = allLessonPlans.find { it.id == lessonPlan1.id }
                val foundLessonPlan2 = allLessonPlans.find { it.id == lessonPlan2.id }

                foundLessonPlan1 shouldNotBe null
                foundLessonPlan2 shouldNotBe null

                foundLessonPlan1 as DraftLessonPlan
                foundLessonPlan2 as DraftLessonPlan

                foundLessonPlan1.title?.value shouldBe "Lesson Plan 1"
                foundLessonPlan1.description?.value shouldBe "Description 1"
                foundLessonPlan1.lessonType shouldBe LessonType.ONLINE
                foundLessonPlan1.location?.value shouldBe "Location 1"
                foundLessonPlan1.annualMaxExecutions shouldBe 10

                foundLessonPlan2.title?.value shouldBe "Lesson Plan 2"
                foundLessonPlan2.description?.value shouldBe "Description 2"
                foundLessonPlan2.lessonType shouldBe LessonType.OFFLINE
                foundLessonPlan2.location?.value shouldBe "Location 2"
                foundLessonPlan2.annualMaxExecutions shouldBe 20
            }
        }
    }
})

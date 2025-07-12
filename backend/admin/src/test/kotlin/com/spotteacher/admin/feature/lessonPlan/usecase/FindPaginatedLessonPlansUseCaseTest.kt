package com.spotteacher.admin.feature.lessonPlan.usecase

import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlan
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanId
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanRepository
import com.spotteacher.admin.fixture.CompanyFixture
import com.spotteacher.admin.fixture.LessonPlanFixture
import com.spotteacher.domain.Pagination
import com.spotteacher.domain.SortOrder
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot

class FindPaginatedLessonPlansUseCaseTest : DescribeSpec({
    describe("FindPaginatedLessonPlansUseCase") {
        // Arrange
        val lessonPlanRepository = mockk<LessonPlanRepository>()
        val useCase = FindPaginatedLessonPlansUseCase(lessonPlanRepository)
        val fixture = LessonPlanFixture()
        val companyFixture = CompanyFixture()

        // Create test data
        val company = companyFixture.buildCompany()
        val lessonPlans = listOf(
            fixture.buildPublishedLessonPlan(companyId = company.id),
            fixture.buildDraftLessonPlan(companyId = company.id),
            fixture.buildPublishedLessonPlan(companyId = company.id)
        )

        describe("call") {
            context("when fetching lesson plans with limit only and no lastId") {
                it("should return lesson plans with the specified limit") {
                    // Arrange
                    val paginationSlot = slot<Pagination<LessonPlan>>()
                    coEvery { lessonPlanRepository.getPaginated(capture(paginationSlot)) } returns lessonPlans.take(2)

                    // Act
                    val result = useCase.call(
                        FindPaginatedLessonPlansUseCaseInput(
                            lastId = Pair(null, SortOrder.ASC),
                            limit = 2
                        )
                    )

                    // Assert
                    result shouldBe lessonPlans.take(2)
                    paginationSlot.captured.limit shouldBe 2
                    paginationSlot.captured.cursorColumns.size shouldBe 1
                }
            }

            context("when fetching lesson plans with limit and lastId with ASC order") {
                it("should return lesson plans after the lastId with the specified limit") {
                    // Arrange
                    val paginationSlot = slot<Pagination<LessonPlan>>()
                    coEvery { lessonPlanRepository.getPaginated(capture(paginationSlot)) } returns lessonPlans.takeLast(2)

                    // Act
                    val result = useCase.call(
                        FindPaginatedLessonPlansUseCaseInput(
                            lastId = Pair(LessonPlanId(1), SortOrder.ASC),
                            limit = 2
                        )
                    )

                    // Assert
                    result shouldBe lessonPlans.takeLast(2)
                    paginationSlot.captured.limit shouldBe 2
                    paginationSlot.captured.cursorColumns.size shouldBe 1
                    paginationSlot.captured.cursorColumns[0].order shouldBe SortOrder.ASC
                    paginationSlot.captured.cursorColumns[0].lastValue shouldBe 1L
                }
            }

            context("when fetching lesson plans with limit and lastId with DESC order") {
                it("should return lesson plans before the lastId with the specified limit") {
                    // Arrange
                    val paginationSlot = slot<Pagination<LessonPlan>>()
                    coEvery { lessonPlanRepository.getPaginated(capture(paginationSlot)) } returns lessonPlans.take(2)

                    // Act
                    val result = useCase.call(
                        FindPaginatedLessonPlansUseCaseInput(
                            lastId = Pair(LessonPlanId(3), SortOrder.DESC),
                            limit = 2
                        )
                    )

                    // Assert
                    result shouldBe lessonPlans.take(2)
                    paginationSlot.captured.limit shouldBe 2
                    paginationSlot.captured.cursorColumns.size shouldBe 1
                    paginationSlot.captured.cursorColumns[0].order shouldBe SortOrder.DESC
                    paginationSlot.captured.cursorColumns[0].lastValue shouldBe 3L
                }
            }

            context("when no lesson plans match the criteria") {
                it("should return an empty list") {
                    // Arrange
                    val paginationSlot = slot<Pagination<LessonPlan>>()
                    coEvery { lessonPlanRepository.getPaginated(capture(paginationSlot)) } returns emptyList()

                    // Act
                    val result = useCase.call(
                        FindPaginatedLessonPlansUseCaseInput(
                            lastId = Pair(LessonPlanId(999), SortOrder.ASC),
                            limit = 10
                        )
                    )

                    // Assert
                    result shouldBe emptyList()
                    paginationSlot.captured.limit shouldBe 10
                    paginationSlot.captured.cursorColumns.size shouldBe 1
                    paginationSlot.captured.cursorColumns[0].order shouldBe SortOrder.ASC
                    paginationSlot.captured.cursorColumns[0].lastValue shouldBe 999L
                }
            }
        }
    }
})

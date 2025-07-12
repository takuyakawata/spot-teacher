package com.spotteacher.teacher.feature.lessonPlan.usecase

import com.spotteacher.domain.Pagination
import com.spotteacher.teacher.feature.lessonPlan.domain.LessonPlan
import com.spotteacher.teacher.feature.lessonPlan.domain.LessonPlanId
import com.spotteacher.teacher.feature.lessonPlan.domain.LessonPlanRepository
import com.spotteacher.teacher.fixture.LessonPlanFixture
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import java.time.LocalDateTime

class FindPaginatedLessonPlansUseCaseTest : DescribeSpec({
    describe("FindPaginatedLessonPlansUseCase") {
        // Arrange
        val lessonPlanRepository = mockk<LessonPlanRepository>()
        val lessonPlanFixture = LessonPlanFixture()
        val useCase = FindPaginatedLessonPlansUseCase(lessonPlanRepository)

        // Test data
        val now = LocalDateTime.now()
        val lessonPlan1 = lessonPlanFixture.buildLessonPlan(
            id = LessonPlanId(1),
            createdAt = now.minusDays(2)
        )
        val lessonPlan2 = lessonPlanFixture.buildLessonPlan(
            id = LessonPlanId(2),
            createdAt = now.minusDays(1)
        )
        val lessonPlan3 = lessonPlanFixture.buildLessonPlan(
            id = LessonPlanId(3),
            createdAt = now
        )
        val lessonPlans = listOf(lessonPlan1, lessonPlan2, lessonPlan3)

        describe("call") {
            it("should return first page of lesson plans when lastId and lastCreatedAt are null") {
                // Arrange
                val paginationSlot = slot<Pagination<LessonPlan>>()
                coEvery { lessonPlanRepository.getPaginated(capture(paginationSlot)) } returns lessonPlans

                // Act
                val result = useCase.call()

                // Assert
                result shouldBe lessonPlans
                paginationSlot.captured.limit shouldBe FindPaginatedLessonPlansUseCase.DEFAULT_LIMIT
                paginationSlot.captured.cursorColumns.isEmpty() shouldBe true

                coVerify { lessonPlanRepository.getPaginated(any()) }
            }

            it("should return lesson plans with custom limit") {
                // Arrange
                val customLimit = 20
                val paginationSlot = slot<Pagination<LessonPlan>>()
                coEvery { lessonPlanRepository.getPaginated(capture(paginationSlot)) } returns lessonPlans

                // Act
                val result = useCase.call(limit = customLimit)

                // Assert
                result shouldBe lessonPlans
                paginationSlot.captured.limit shouldBe customLimit
                paginationSlot.captured.cursorColumns.isEmpty() shouldBe true

                coVerify { lessonPlanRepository.getPaginated(any()) }
            }

            it("should return next page of lesson plans when lastId and lastCreatedAt are provided") {
                // Arrange
                val lastId = LessonPlanId(2)
                val lastCreatedAt = now.minusDays(1)
                val paginationSlot = slot<Pagination<LessonPlan>>()
                coEvery { lessonPlanRepository.getPaginated(capture(paginationSlot)) } returns lessonPlans.subList(2, 3)

                // Act
                val result = useCase.call(lastId = lastId, lastCreatedAt = lastCreatedAt)

                // Assert
                result shouldBe lessonPlans.subList(2, 3)
                paginationSlot.captured.limit shouldBe FindPaginatedLessonPlansUseCase.DEFAULT_LIMIT
                paginationSlot.captured.cursorColumns.size shouldBe 2

                coVerify { lessonPlanRepository.getPaginated(any()) }
            }
        }
    }
})
package com.spotteacher.admin.feature.lessonSchedule.usecase

import com.spotteacher.admin.feature.lessonPlan.domain.LessonType
import com.spotteacher.admin.feature.lessonReservation.domain.LessonReservationDescription
import com.spotteacher.admin.feature.lessonReservation.domain.LessonReservationEducations
import com.spotteacher.admin.feature.lessonReservation.domain.LessonReservationGrades
import com.spotteacher.admin.feature.lessonReservation.domain.LessonReservationId
import com.spotteacher.admin.feature.lessonReservation.domain.LessonReservationSubjects
import com.spotteacher.admin.feature.lessonReservation.domain.LessonReservationTitle
import com.spotteacher.admin.feature.lessonReservation.domain.ReservationLessonLocation
import com.spotteacher.admin.feature.lessonSchedule.domain.DoingLessonSchedule
import com.spotteacher.admin.feature.lessonSchedule.domain.LessonSchedule
import com.spotteacher.admin.feature.lessonSchedule.domain.LessonScheduleId
import com.spotteacher.admin.feature.lessonSchedule.domain.LessonScheduleRepository
import com.spotteacher.admin.feature.lessonSchedule.domain.ScheduleDate
import com.spotteacher.admin.feature.lessonTag.domain.EducationId
import com.spotteacher.admin.feature.lessonTag.domain.Grade
import com.spotteacher.admin.feature.lessonTag.domain.Subject
import com.spotteacher.admin.feature.school.domain.SchoolId
import com.spotteacher.admin.feature.teacher.domain.TeacherId
import com.spotteacher.domain.Pagination
import com.spotteacher.domain.SortOrder
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import java.sql.Time
import java.time.LocalDate
import java.time.LocalTime

class FindPaginatedLessonScheduleUseCaseTest : DescribeSpec({
    describe("FindPaginatedLessonScheduleUseCase") {
        // Arrange
        val lessonScheduleRepository = mockk<LessonScheduleRepository>()
        val useCase = FindPaginatedLessonScheduleUseCase(lessonScheduleRepository)

        // Create test data
        val scheduleDate = ScheduleDate(
            date = LocalDate.now(),
            startTime = LocalTime.of(10, 0),
            endTime = Time.valueOf(LocalTime.of(11, 0))
        )

        val lessonSchedules = listOf(
            DoingLessonSchedule(
                id = LessonScheduleId(1L),
                lessonReservationId = LessonReservationId(101L),
                reservedSchoolId = SchoolId(201L),
                reservedTeacherId = TeacherId(301L),
                scheduleDate = scheduleDate,
                educations = LessonReservationEducations(setOf(EducationId(1L))),
                subjects = LessonReservationSubjects(setOf(Subject.MATH)),
                grades = LessonReservationGrades(setOf(Grade.ELEMENTARY_1)),
                title = LessonReservationTitle("Lesson 1"),
                description = LessonReservationDescription("Description 1"),
                lessonType = LessonType.OFFLINE,
                location = ReservationLessonLocation("Location 1")
            ),
            DoingLessonSchedule(
                id = LessonScheduleId(2L),
                lessonReservationId = LessonReservationId(102L),
                reservedSchoolId = SchoolId(202L),
                reservedTeacherId = TeacherId(302L),
                scheduleDate = scheduleDate,
                educations = LessonReservationEducations(setOf(EducationId(2L))),
                subjects = LessonReservationSubjects(setOf(Subject.ENGLISH)),
                grades = LessonReservationGrades(setOf(Grade.ELEMENTARY_2)),
                title = LessonReservationTitle("Lesson 2"),
                description = LessonReservationDescription("Description 2"),
                lessonType = LessonType.ONLINE,
                location = ReservationLessonLocation("Location 2")
            ),
            DoingLessonSchedule(
                id = LessonScheduleId(3L),
                lessonReservationId = LessonReservationId(103L),
                reservedSchoolId = SchoolId(203L),
                reservedTeacherId = TeacherId(303L),
                scheduleDate = scheduleDate,
                educations = LessonReservationEducations(setOf(EducationId(3L))),
                subjects = LessonReservationSubjects(setOf(Subject.SCIENCE)),
                grades = LessonReservationGrades(setOf(Grade.ELEMENTARY_3)),
                title = LessonReservationTitle("Lesson 3"),
                description = LessonReservationDescription("Description 3"),
                lessonType = LessonType.OFFLINE,
                location = ReservationLessonLocation("Location 3")
            )
        )

        describe("call") {
            context("when fetching lesson schedules with limit only and no lastId") {
                it("should return lesson schedules with the specified limit") {
                    // Arrange
                    val paginationSlot = slot<Pagination<LessonSchedule>>()
                    coEvery { lessonScheduleRepository.getAll(capture(paginationSlot)) } returns lessonSchedules.take(2)

                    // Act
                    val result = useCase.call(
                        FindPaginatedLessonScheduleUseCaseInput(
                            lastId = Pair(null, SortOrder.ASC),
                            limit = 2
                        )
                    )

                    // Assert
                    result shouldBe lessonSchedules.take(2)
                    paginationSlot.captured.limit shouldBe 2
                    paginationSlot.captured.cursorColumns.size shouldBe 1
                    paginationSlot.captured.cursorColumns[0].lastValue shouldBe null
                    paginationSlot.captured.cursorColumns[0].order shouldBe SortOrder.ASC
                }
            }

            context("when fetching lesson schedules with limit and lastId with ASC order") {
                it("should return lesson schedules after the lastId with the specified limit") {
                    // Arrange
                    val paginationSlot = slot<Pagination<LessonSchedule>>()
                    coEvery { lessonScheduleRepository.getAll(capture(paginationSlot)) } returns lessonSchedules.takeLast(2)

                    // Act
                    val result = useCase.call(
                        FindPaginatedLessonScheduleUseCaseInput(
                            lastId = Pair(LessonScheduleId(1L), SortOrder.ASC),
                            limit = 2
                        )
                    )

                    // Assert
                    result shouldBe lessonSchedules.takeLast(2)
                    paginationSlot.captured.limit shouldBe 2
                    paginationSlot.captured.cursorColumns.size shouldBe 1
                    paginationSlot.captured.cursorColumns[0].lastValue shouldBe 1L
                    paginationSlot.captured.cursorColumns[0].order shouldBe SortOrder.ASC
                }
            }

            context("when fetching lesson schedules with limit and lastId with DESC order") {
                it("should return lesson schedules before the lastId with the specified limit") {
                    // Arrange
                    val paginationSlot = slot<Pagination<LessonSchedule>>()
                    coEvery { lessonScheduleRepository.getAll(capture(paginationSlot)) } returns lessonSchedules.take(2)

                    // Act
                    val result = useCase.call(
                        FindPaginatedLessonScheduleUseCaseInput(
                            lastId = Pair(LessonScheduleId(3L), SortOrder.DESC),
                            limit = 2
                        )
                    )

                    // Assert
                    result shouldBe lessonSchedules.take(2)
                    paginationSlot.captured.limit shouldBe 2
                    paginationSlot.captured.cursorColumns.size shouldBe 1
                    paginationSlot.captured.cursorColumns[0].lastValue shouldBe 3L
                    paginationSlot.captured.cursorColumns[0].order shouldBe SortOrder.DESC
                }
            }

            context("when no lesson schedules match the criteria") {
                it("should return an empty list") {
                    // Arrange
                    val paginationSlot = slot<Pagination<LessonSchedule>>()
                    coEvery { lessonScheduleRepository.getAll(capture(paginationSlot)) } returns emptyList()

                    // Act
                    val result = useCase.call(
                        FindPaginatedLessonScheduleUseCaseInput(
                            lastId = Pair(LessonScheduleId(999L), SortOrder.ASC),
                            limit = 10
                        )
                    )

                    // Assert
                    result shouldBe emptyList()
                    paginationSlot.captured.limit shouldBe 10
                    paginationSlot.captured.cursorColumns.size shouldBe 1
                    paginationSlot.captured.cursorColumns[0].lastValue shouldBe 999L
                    paginationSlot.captured.cursorColumns[0].order shouldBe SortOrder.ASC
                }
            }
        }
    }
})
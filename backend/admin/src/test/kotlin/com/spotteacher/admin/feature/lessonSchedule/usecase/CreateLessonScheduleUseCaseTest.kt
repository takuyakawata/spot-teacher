package com.spotteacher.admin.feature.lessonSchedule.usecase

import arrow.core.left
import arrow.core.right
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanId
import com.spotteacher.admin.feature.lessonPlan.domain.LessonType
import com.spotteacher.admin.feature.lessonReservation.domain.LessonReservation
import com.spotteacher.admin.feature.lessonReservation.domain.LessonReservationDate
import com.spotteacher.admin.feature.lessonReservation.domain.LessonReservationDates
import com.spotteacher.admin.feature.lessonReservation.domain.LessonReservationDescription
import com.spotteacher.admin.feature.lessonReservation.domain.LessonReservationEducations
import com.spotteacher.admin.feature.lessonReservation.domain.LessonReservationGrades
import com.spotteacher.admin.feature.lessonReservation.domain.LessonReservationId
import com.spotteacher.admin.feature.lessonReservation.domain.LessonReservationRepository
import com.spotteacher.admin.feature.lessonReservation.domain.LessonReservationSubjects
import com.spotteacher.admin.feature.lessonReservation.domain.LessonReservationTitle
import com.spotteacher.admin.feature.lessonReservation.domain.ReservationLessonLocation
import com.spotteacher.admin.feature.lessonSchedule.domain.DoingLessonSchedule
import com.spotteacher.admin.feature.lessonSchedule.domain.LessonScheduleError
import com.spotteacher.admin.feature.lessonSchedule.domain.LessonScheduleErrorCode
import com.spotteacher.admin.feature.lessonSchedule.domain.LessonScheduleId
import com.spotteacher.admin.feature.lessonSchedule.domain.LessonScheduleRepository
import com.spotteacher.admin.feature.lessonSchedule.domain.ScheduleDate
import com.spotteacher.admin.feature.lessonTag.domain.EducationId
import com.spotteacher.admin.feature.lessonTag.domain.Grade
import com.spotteacher.admin.feature.lessonTag.domain.Subject
import com.spotteacher.admin.feature.school.domain.SchoolId
import com.spotteacher.admin.feature.teacher.domain.TeacherId
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import arrow.core.nonEmptyListOf
import java.sql.Time
import java.time.LocalDate
import java.time.LocalTime

class CreateLessonScheduleUseCaseTest : DescribeSpec({
    describe("CreateLessonScheduleUseCase") {
        // Arrange
        val lessonReservationRepository = mockk<LessonReservationRepository>()
        val lessonScheduleRepository = mockk<LessonScheduleRepository>()
        val useCase = CreateLessonScheduleUseCase(
            lessonReservationRepository = lessonReservationRepository,
            lessonScheduleRepository = lessonScheduleRepository
        )

        // Test data
        val lessonReservationId = LessonReservationId(1L)
        val schoolId = SchoolId(2L)
        val teacherId = TeacherId(3L)
        val lessonPlanId = LessonPlanId(4L)
        val educations = LessonReservationEducations(setOf(EducationId(1L)))
        val subjects = LessonReservationSubjects(setOf(Subject.MATH))
        val grades = LessonReservationGrades(setOf(Grade.ELEMENTARY_1))
        val title = LessonReservationTitle("Test Lesson")
        val description = LessonReservationDescription("Test Description")
        val lessonType = LessonType.OFFLINE
        val location = ReservationLessonLocation("Test Location")
        
        val reservationDate = LessonReservationDate(
            date = LocalDate.now(),
            startTime = LocalTime.of(10, 0),
            endTime = LocalTime.of(11, 0)
        )
        val reservationDates = LessonReservationDates(nonEmptyListOf(reservationDate))

        val lessonReservation = LessonReservation(
            id = lessonReservationId,
            lessonPlanId = lessonPlanId,
            reservedSchoolId = schoolId,
            reservedTeacherId = teacherId,
            educations = educations,
            subjects = subjects,
            grades = grades,
            title = title,
            description = description,
            lessonType = lessonType,
            location = location,
            reservationDates = reservationDates
        )

        val scheduleDate = ScheduleDate(
            date = LocalDate.now(),
            startTime = LocalTime.of(10, 0),
            endTime = Time.valueOf(LocalTime.of(11, 0))
        )

        val doingLessonSchedule = DoingLessonSchedule(
            id = LessonScheduleId(0),
            lessonReservationId = lessonReservationId,
            reservedSchoolId = schoolId,
            reservedTeacherId = teacherId,
            scheduleDate = scheduleDate,
            educations = educations,
            subjects = subjects,
            grades = grades,
            title = title,
            description = description,
            lessonType = lessonType,
            location = location
        )

        describe("call") {
            context("when lesson reservation is found") {
                it("should create a new lesson schedule and return success") {
                    // Arrange
                    coEvery { lessonReservationRepository.findById(lessonReservationId) } returns lessonReservation
                    coEvery { lessonScheduleRepository.create(any()) } returns doingLessonSchedule

                    // Act
                    val result = useCase.call(
                        CreateLessonScheduleUseCaseInput(
                            lessonReservationId = lessonReservationId,
                            scheduleDate = scheduleDate
                        )
                    )

                    // Assert
                    result.isRight() shouldBe true
                    
                    // Verify that the repository methods were called
                    coVerify { lessonReservationRepository.findById(lessonReservationId) }
                    coVerify { lessonScheduleRepository.create(any()) }
                }
            }

            context("when lesson reservation is not found") {
                it("should return an error") {
                    // Arrange
                    coEvery { lessonReservationRepository.findById(lessonReservationId) } returns null

                    // Act
                    val result = useCase.call(
                        CreateLessonScheduleUseCaseInput(
                            lessonReservationId = lessonReservationId,
                            scheduleDate = scheduleDate
                        )
                    )

                    // Assert
                    result.isLeft() shouldBe true
                    
                    val expectedError = LessonScheduleError(
                        message = "Lesson reservation with ID ${lessonReservationId.value} not found.",
                        errorCode = LessonScheduleErrorCode.LESSON_RESERVATION_NOT_FOUND
                    )
                    result shouldBe expectedError.left()
                    
                    // Verify that only the findById method was called
                    coVerify { lessonReservationRepository.findById(lessonReservationId) }
                }
            }
        }
    }
})

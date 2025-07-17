package com.spotteacher.admin.feature.lessonSchedule.usecase

import arrow.core.left
import arrow.core.right
import com.spotteacher.admin.feature.lessonPlan.domain.LessonType
import com.spotteacher.admin.feature.lessonReservation.domain.LessonReservationDescription
import com.spotteacher.admin.feature.lessonReservation.domain.LessonReservationEducations
import com.spotteacher.admin.feature.lessonReservation.domain.LessonReservationGrades
import com.spotteacher.admin.feature.lessonReservation.domain.LessonReservationId
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
import java.sql.Time
import java.time.LocalDate
import java.time.LocalTime

class FindLessonScheduleUseCaseTest : DescribeSpec({
    describe("FindLessonScheduleUseCase") {
        // Arrange
        val lessonScheduleRepository = mockk<LessonScheduleRepository>()
        val useCase = FindLessonScheduleUseCase(
            repository = lessonScheduleRepository
        )

        // Test data
        val lessonScheduleId = LessonScheduleId(1L)
        val lessonReservationId = LessonReservationId(2L)
        val schoolId = SchoolId(3L)
        val teacherId = TeacherId(4L)
        val educations = LessonReservationEducations(setOf(EducationId(1L)))
        val subjects = LessonReservationSubjects(setOf(Subject.MATH))
        val grades = LessonReservationGrades(setOf(Grade.ELEMENTARY_1))
        val title = LessonReservationTitle("Test Lesson")
        val description = LessonReservationDescription("Test Description")
        val lessonType = LessonType.OFFLINE
        val location = ReservationLessonLocation("Test Location")
        
        val scheduleDate = ScheduleDate(
            date = LocalDate.now(),
            startTime = LocalTime.of(10, 0),
            endTime = Time.valueOf(LocalTime.of(11, 0))
        )

        val doingLessonSchedule = DoingLessonSchedule(
            id = lessonScheduleId,
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
            context("when lesson schedule is found") {
                it("should return the lesson schedule") {
                    // Arrange
                    coEvery { lessonScheduleRepository.findById(lessonScheduleId) } returns doingLessonSchedule

                    // Act
                    val result = useCase.call(lessonScheduleId)

                    // Assert
                    result.isRight() shouldBe true
                    result shouldBe doingLessonSchedule.right()
                    
                    // Verify that the repository method was called
                    coVerify { lessonScheduleRepository.findById(lessonScheduleId) }
                }
            }

            context("when lesson schedule is not found") {
                it("should return an error") {
                    // Arrange
                    coEvery { lessonScheduleRepository.findById(lessonScheduleId) } returns null

                    // Act
                    val result = useCase.call(lessonScheduleId)

                    // Assert
                    result.isLeft() shouldBe true
                    
                    val expectedError = LessonScheduleError(
                        message = "Lesson schedule with ID ${lessonScheduleId.value} not found",
                        errorCode = LessonScheduleErrorCode.LESSON_SCHEDULE_NOT_FOUND
                    )
                    result shouldBe expectedError.left()
                    
                    // Verify that the repository method was called
                    coVerify { lessonScheduleRepository.findById(lessonScheduleId) }
                }
            }
        }
    }
})
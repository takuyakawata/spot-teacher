package com.spotteacher.teacher.feature.lessonReservation.usecase

import arrow.core.nonEmptyListOf
import com.spotteacher.teacher.feature.lessonPlan.domain.LessonPlanId
import com.spotteacher.teacher.feature.lessonPlan.domain.LessonType
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservation
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservationDate
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservationDates
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservationDescription
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservationEducations
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservationGrades
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservationId
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservationRepository
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservationSubjects
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservationTitle
import com.spotteacher.teacher.feature.lessonReservation.domain.ReservationLessonLocation
import com.spotteacher.teacher.feature.lessonReservation.domain.TeacherId
import com.spotteacher.teacher.feature.lessonReservation.handler.LessonReservationType
import com.spotteacher.teacher.feature.lessonTag.domain.EducationId
import com.spotteacher.teacher.feature.lessonTag.domain.Grade
import com.spotteacher.teacher.feature.lessonTag.domain.Subject
import com.spotteacher.teacher.feature.school.domain.SchoolId
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import java.time.LocalDate
import java.time.LocalTime

class CreateLessonReservationUseCaseTest : FunSpec({

    val lessonReservationRepository = mockk<LessonReservationRepository>()
    val createLessonReservationUseCase = CreateLessonReservationUseCase(lessonReservationRepository)

    test("should create lesson reservation successfully") {
        // Given
        val teacherId = TeacherId(1L)
        val schoolId = SchoolId(2L)
        val lessonPlanId = LessonPlanId(3L)
        val title = LessonReservationTitle("Test Lesson Reservation")
        val description = LessonReservationTitle("Test Description")
        val location = ReservationLessonLocation("Test Location")
        val lessonTypeEnum = LessonType.ONLINE
        val lessonType = mockk<LessonReservationType>()
        every<LessonType> { lessonType.lessonType } returns lessonTypeEnum
        val educations = LessonReservationEducations(setOf(EducationId(1L)))
        val subjects = LessonReservationSubjects(setOf(Subject.MATH))
        val grades = LessonReservationGrades(setOf(Grade.ELEMENTARY_1))

        val reservationDate = LessonReservationDate(
            date = LocalDate.now(),
            startTime = LocalTime.of(10, 0),
            endTime = LocalTime.of(11, 0)
        )
        val reservationDates = LessonReservationDates(nonEmptyListOf(reservationDate))

        val input = CreateLessonReservationUseCaseInput(
            teacherId = teacherId,
            schoolId = schoolId,
            lessonPlanId = lessonPlanId,
            title = title,
            description = description,
            location = location,
            lessonType = lessonType,
            educations = educations,
            subjects = subjects,
            grades = grades,
            lessonReservationDates = reservationDates
        )

        val expectedLessonReservation = LessonReservation(
            id = LessonReservationId(100L),
            lessonPlanId = lessonPlanId,
            reservedSchoolId = schoolId,
            reservedTeacherId = teacherId,
            educations = educations,
            subjects = subjects,
            grades = grades,
            title = title,
            description = LessonReservationDescription(description.value),
            lessonType = lessonTypeEnum,
            location = location,
            reservationDates = reservationDates
        )

        val lessonReservationSlot = slot<LessonReservation>()
        coEvery { lessonReservationRepository.create(capture(lessonReservationSlot)) } returns expectedLessonReservation

        // When
        val result = createLessonReservationUseCase.call(input)

        // Then
        result shouldBe expectedLessonReservation
        coVerify { lessonReservationRepository.create(any()) }

        // Verify the captured lesson reservation has the correct properties
        val capturedLessonReservation = lessonReservationSlot.captured
        capturedLessonReservation.lessonPlanId shouldBe lessonPlanId
        capturedLessonReservation.reservedSchoolId shouldBe schoolId
        capturedLessonReservation.reservedTeacherId shouldBe teacherId
        capturedLessonReservation.educations shouldBe educations
        capturedLessonReservation.subjects shouldBe subjects
        capturedLessonReservation.grades shouldBe grades
        capturedLessonReservation.title shouldBe title
        capturedLessonReservation.description shouldBe LessonReservationDescription(description.value)
        capturedLessonReservation.lessonType shouldBe lessonTypeEnum
        capturedLessonReservation.location shouldBe location
        capturedLessonReservation.reservationDates shouldBe reservationDates
    }
})

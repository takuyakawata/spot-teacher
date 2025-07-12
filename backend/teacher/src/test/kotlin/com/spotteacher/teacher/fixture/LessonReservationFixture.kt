package com.spotteacher.teacher.fixture

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
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservationSubjects
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservationTitle
import com.spotteacher.teacher.feature.lessonReservation.domain.ReservationLessonLocation
import com.spotteacher.teacher.feature.lessonReservation.domain.TeacherId
import com.spotteacher.teacher.feature.lessonTag.domain.EducationId
import com.spotteacher.teacher.feature.lessonTag.domain.Grade
import com.spotteacher.teacher.feature.lessonTag.domain.Subject
import com.spotteacher.teacher.feature.school.domain.SchoolId
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalTime

@Component
class LessonReservationFixture(
    private val lessonPlanFixture: LessonPlanFixture
) {

    private var lessonReservationIdCount = 1L

    fun buildLessonReservation(
        id: LessonReservationId = LessonReservationId(lessonReservationIdCount++),
        lessonPlanId: LessonPlanId = lessonPlanFixture.buildLessonPlan().id,
        reservedSchoolId: SchoolId = SchoolId(1),
        reservedTeacherId: TeacherId = TeacherId(1),
        educations: Set<EducationId> = emptySet(),
        subjects: Set<Subject> = emptySet(),
        grades: Set<Grade> = emptySet(),
        title: String = "Test Lesson Reservation",
        description: String = "Test Description",
        lessonType: LessonType = LessonType.OFFLINE,
        location: String = "Test Location",
        date: LocalDate = LocalDate.now().plusDays(7),
        startTime: LocalTime = LocalTime.of(9, 0),
        endTime: LocalTime = LocalTime.of(17, 0)
    ): LessonReservation {
        return LessonReservation(
            id = id,
            lessonPlanId = lessonPlanId,
            reservedSchoolId = reservedSchoolId,
            reservedTeacherId = reservedTeacherId,
            educations = LessonReservationEducations(educations),
            subjects = LessonReservationSubjects(subjects),
            grades = LessonReservationGrades(grades),
            title = LessonReservationTitle(title),
            description = LessonReservationDescription(description),
            lessonType = lessonType,
            location = ReservationLessonLocation(location),
            reservationDates = LessonReservationDates(nonEmptyListOf(
                LessonReservationDate(
                    date = date,
                    startTime = startTime,
                    endTime = endTime
                )
            ))
        )
    }
}

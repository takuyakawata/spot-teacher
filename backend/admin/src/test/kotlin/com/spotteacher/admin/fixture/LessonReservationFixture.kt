package com.spotteacher.admin.fixture

import arrow.core.nonEmptyListOf
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
import com.spotteacher.admin.feature.lessonTag.domain.EducationId
import com.spotteacher.admin.feature.lessonTag.domain.Grade
import com.spotteacher.admin.feature.lessonTag.domain.Subject
import com.spotteacher.admin.feature.school.domain.SchoolId
import com.spotteacher.admin.feature.teacher.domain.TeacherId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalTime

@Component
class LessonReservationFixture {

    @Autowired
    private lateinit var repository: LessonReservationRepository

    private var lessonReservationIdCount = 1L

    fun buildLessonReservation(
        id: LessonReservationId = LessonReservationId(lessonReservationIdCount++),
        lessonPlanId: LessonPlanId,
        reservedSchoolId: SchoolId,
        reservedTeacherId: TeacherId,
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

    suspend fun createLessonReservation(
        lessonPlanId: LessonPlanId,
        reservedSchoolId: SchoolId,
        reservedTeacherId: TeacherId,
        educations: Set<EducationId> = emptySet(),
        subjects: Set<Subject> = emptySet(),
        grades: Set<Grade> = emptySet(),
        title: String = "Test Lesson Reservation",
        description: String = "Test Description",
        lessonType: LessonType = LessonType.OFFLINE,
        location: String = "Test Location"
    ): LessonReservation {
        // Create a lesson reservation with default values
        val lessonReservation = buildLessonReservation(
            lessonPlanId = lessonPlanId,
            reservedSchoolId = reservedSchoolId,
            reservedTeacherId = reservedTeacherId,
            educations = educations,
            subjects = subjects,
            grades = grades,
            title = title,
            description = description,
            lessonType = lessonType,
            location = location
        )

        return repository.create(lessonReservation)
    }
}
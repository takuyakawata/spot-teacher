package com.spotteacher.teacher.fixture

import com.spotteacher.teacher.feature.lessonPlan.domain.LessonType
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservationDescription
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservationEducations
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservationGrades
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservationId
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservationSubjects
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservationTitle
import com.spotteacher.teacher.feature.lessonReservation.domain.ReservationLessonLocation
import com.spotteacher.teacher.feature.lessonSchedule.domain.DoingLessonSchedule
import com.spotteacher.teacher.feature.lessonSchedule.domain.LessonScheduleId
import com.spotteacher.teacher.feature.lessonSchedule.domain.LessonScheduleRepository
import com.spotteacher.teacher.feature.lessonSchedule.domain.ScheduleDate
import com.spotteacher.teacher.feature.lessonTag.domain.EducationId
import com.spotteacher.teacher.feature.lessonTag.domain.Grade
import com.spotteacher.teacher.feature.lessonTag.domain.Subject
import com.spotteacher.teacher.feature.school.domain.SchoolId
import com.spotteacher.teacher.feature.teacher.domain.TeacherId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.sql.Time
import java.time.LocalDate
import java.time.LocalTime

@Component
class LessonScheduleFixture {

    @Autowired
    private lateinit var repository: LessonScheduleRepository

    private var lessonScheduleIdCount = 1L

    fun buildDoingLessonSchedule(
        id: LessonScheduleId = LessonScheduleId(lessonScheduleIdCount++),
        lessonReservationId: LessonReservationId,
        reservedSchoolId: SchoolId,
        reservedTeacherId: TeacherId,
        educations: Set<EducationId> = emptySet(),
        subjects: Set<Subject> = emptySet(),
        grades: Set<Grade> = emptySet(),
        title: String = "Test Lesson Schedule",
        description: String = "Test Description",
        lessonType: LessonType = LessonType.OFFLINE,
        location: String = "Test Location",
        date: LocalDate = LocalDate.now().plusDays(7),
        startTime: LocalTime = LocalTime.of(9, 0),
        endTime: LocalTime = LocalTime.of(17, 0)
    ): DoingLessonSchedule {
        return DoingLessonSchedule(
            id = id,
            lessonReservationId = lessonReservationId,
            reservedSchoolId = reservedSchoolId,
            reservedTeacherId = reservedTeacherId,
            scheduleDate = ScheduleDate(
                date = date,
                startTime = startTime,
                endTime = Time.valueOf(endTime)
            ),
            educations = LessonReservationEducations(educations),
            subjects = LessonReservationSubjects(subjects),
            grades = LessonReservationGrades(grades),
            title = LessonReservationTitle(title),
            description = LessonReservationDescription(description),
            lessonType = lessonType,
            location = ReservationLessonLocation(location)
        )
    }

    suspend fun create(
        lessonReservationId: LessonReservationId,
        reservedSchoolId: SchoolId,
        reservedTeacherId: TeacherId
    ): DoingLessonSchedule {
        val lessonSchedule = buildDoingLessonSchedule(
            id = LessonScheduleId(0),
            lessonReservationId = lessonReservationId,
            reservedSchoolId = reservedSchoolId,
            reservedTeacherId = reservedTeacherId
        )
        return repository.create(lessonSchedule)
    }
}
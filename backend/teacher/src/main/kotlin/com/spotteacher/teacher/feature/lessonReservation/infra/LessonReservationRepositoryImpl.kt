package com.spotteacher.teacher.feature.lessonReservation.infra

import arrow.core.Nel
import arrow.core.nonEmptyListOf
import com.spotteacher.infra.db.enums.LessonReservationsLessonType
import com.spotteacher.infra.db.tables.LessonReservationDates.Companion.LESSON_RESERVATION_DATES
import com.spotteacher.infra.db.tables.LessonReservationEducations.Companion.LESSON_RESERVATION_EDUCATIONS
import com.spotteacher.infra.db.tables.LessonReservationGrades.Companion.LESSON_RESERVATION_GRADES
import com.spotteacher.infra.db.tables.LessonReservationSubjects.Companion.LESSON_RESERVATION_SUBJECTS
import com.spotteacher.infra.db.tables.LessonReservations.Companion.LESSON_RESERVATIONS
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
import com.spotteacher.teacher.feature.school.domain.SchoolId
import com.spotteacher.teacher.shared.infra.TransactionAwareDSLContext
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitLast
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class LessonReservationRepositoryImpl(private val dslContext: TransactionAwareDSLContext) : LessonReservationRepository {

    override suspend fun create(lessonReservation: LessonReservation): LessonReservation {
        // Create a LessonReservation with the provided values
        val now = LocalDateTime.now()
        val lessonReservationId = dslContext.get().insertInto(
            LESSON_RESERVATIONS,
            LESSON_RESERVATIONS.COMPANY_ID,
            LESSON_RESERVATIONS.LESSON_PLAN_ID,
            LESSON_RESERVATIONS.SCHOOL_ID,
            LESSON_RESERVATIONS.TEACHER_ID,
            LESSON_RESERVATIONS.TITLE,
            LESSON_RESERVATIONS.DESCRIPTION,
            LESSON_RESERVATIONS.LOCATION,
            LESSON_RESERVATIONS.LESSON_TYPE,
            LESSON_RESERVATIONS.CREATED_AT,
            LESSON_RESERVATIONS.UPDATED_AT
        ).values(
            1L, // Default company ID, should be updated with actual company ID
            lessonReservation.lessonPlanId.value,
            lessonReservation.reservedSchoolId.value,
            lessonReservation.reservedTeacherId.value,
            lessonReservation.title.value,
            lessonReservation.description.value,
            lessonReservation.location.value,
            when (lessonReservation.lessonType) {
                LessonType.ONLINE -> LessonReservationsLessonType.ONLINE
                LessonType.OFFLINE -> LessonReservationsLessonType.OFFLINE
                LessonType.ONLINE_AND_OFFLINE -> LessonReservationsLessonType.ONLINE_AND_OFFLINE
            },
            now,
            now
        ).returning(LESSON_RESERVATIONS.ID).awaitFirstOrNull()?.id!!

        // Insert the reservation dates using batch operation
        val dateQueries = lessonReservation.reservationDates.value.map { date ->
            dslContext.get().insertInto(
                LESSON_RESERVATION_DATES,
                LESSON_RESERVATION_DATES.LESSON_RESERVATION_ID,
                LESSON_RESERVATION_DATES.START_DATE,
                LESSON_RESERVATION_DATES.START_TIME,
                LESSON_RESERVATION_DATES.END_TIME,
                LESSON_RESERVATION_DATES.CREATED_AT,
                LESSON_RESERVATION_DATES.UPDATED_AT
            ).values(
                lessonReservationId,
                date.date,
                LocalDateTime.of(date.date, date.startTime),
                LocalDateTime.of(date.date, date.endTime),
                now,
                now
            )
        }
        if (dateQueries.isNotEmpty()) {
            dslContext.get().batch(dateQueries).awaitLast()
        }

        // Insert education IDs using batch operation
        val educationQueries = lessonReservation.educations.value.map { educationId ->
            dslContext.get().insertInto(
                LESSON_RESERVATION_EDUCATIONS,
                LESSON_RESERVATION_EDUCATIONS.LESSON_RESERVATION_ID,
                LESSON_RESERVATION_EDUCATIONS.EDUCATION_ID,
                LESSON_RESERVATION_EDUCATIONS.CREATED_AT,
                LESSON_RESERVATION_EDUCATIONS.UPDATED_AT
            ).values(
                lessonReservationId,
                educationId.value,
                now,
                now
            )
        }
        if (educationQueries.isNotEmpty()) {
            dslContext.get().batch(educationQueries).awaitLast()
        }

        // Insert subjects using batch operation
        val subjectQueries = lessonReservation.subjects.value.map { subject ->
            dslContext.get().insertInto(
                LESSON_RESERVATION_SUBJECTS,
                LESSON_RESERVATION_SUBJECTS.LESSON_RESERVATION_ID,
                LESSON_RESERVATION_SUBJECTS.SUBJECT_CODE,
                LESSON_RESERVATION_SUBJECTS.DISPLAY_ORDER
            ).values(
                lessonReservationId,
                subject.name,
                0 // Default display order
            )
        }
        if (subjectQueries.isNotEmpty()) {
            dslContext.get().batch(subjectQueries).awaitLast()
        }

        // Insert grades using batch operation
        val gradeQueries = lessonReservation.grades.value.map { grade ->
            dslContext.get().insertInto(
                LESSON_RESERVATION_GRADES,
                LESSON_RESERVATION_GRADES.LESSON_RESERVATION_ID,
                LESSON_RESERVATION_GRADES.GRADE_CODE,
                LESSON_RESERVATION_GRADES.DISPLAY_ORDER
            ).values(
                lessonReservationId,
                grade.name,
                0 // Default display order
            )
        }
        if (gradeQueries.isNotEmpty()) {
            dslContext.get().batch(gradeQueries).awaitLast()
        }

        // Return the created LessonReservation with the generated ID
        return lessonReservation.copy(id = LessonReservationId(lessonReservationId))
    }
}

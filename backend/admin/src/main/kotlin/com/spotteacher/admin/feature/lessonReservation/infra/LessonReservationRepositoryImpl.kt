package com.spotteacher.admin.feature.lessonReservation.infra

import arrow.core.Nel
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
import com.spotteacher.admin.shared.infra.TransactionAwareDSLContext
import com.spotteacher.domain.Pagination
import com.spotteacher.domain.SortOrder
import com.spotteacher.extension.nonBlockingFetch
import com.spotteacher.extension.nonBlockingFetchOne
import com.spotteacher.infra.db.enums.LessonReservationsLessonType
import com.spotteacher.infra.db.tables.LessonReservationDates.Companion.LESSON_RESERVATION_DATES
import com.spotteacher.infra.db.tables.LessonReservationEducations.Companion.LESSON_RESERVATION_EDUCATIONS
import com.spotteacher.infra.db.tables.LessonReservationGrades.Companion.LESSON_RESERVATION_GRADES
import com.spotteacher.infra.db.tables.LessonReservationSubjects.Companion.LESSON_RESERVATION_SUBJECTS
import com.spotteacher.infra.db.tables.LessonReservations.Companion.LESSON_RESERVATIONS
import com.spotteacher.infra.db.tables.records.LessonReservationsRecord
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitLast
import org.jetbrains.annotations.TestOnly
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Repository
class LessonReservationRepositoryImpl(private val dslContext: TransactionAwareDSLContext) : LessonReservationRepository {
    override suspend fun paginated(pagination: Pagination<LessonReservation>): List<LessonReservation> {
        val paginationFields = pagination.cursorColumns.mapNotNull { column ->
            LESSON_RESERVATIONS.field(column.getDbColumnName())?.let { field ->
                when (column.order) {
                    SortOrder.ASC -> field.asc()
                    SortOrder.DESC -> field.desc()
                } to column.getPrimitiveValue()
            }
        }

        val query = dslContext.get().selectFrom(LESSON_RESERVATIONS)

        // Apply pagination if there are cursor columns
        val paginatedQuery = if (paginationFields.isNotEmpty()) {
            query.orderBy(*paginationFields.map { it.first }.toTypedArray())
                .seek(*paginationFields.map { it.second }.toTypedArray())
                .limit(pagination.limit)
        } else {
            query.limit(pagination.limit)
        }

        // Use reactive flow to handle suspension functions properly
        val lessonReservationFlow = paginatedQuery.asFlow()
            .map { record ->
                val recordId = record.id ?: throw IllegalStateException("Lesson reservation ID cannot be null")
                val dates = getLessonReservationDates(recordId)
                val educations = getLessonReservationEducations(recordId)
                val subjects = getLessonReservationSubjects(recordId)
                val grades = getLessonReservationGrades(recordId)
                record.into(LessonReservationsRecord::class.java).toEntity(dates, educations, subjects, grades)
            }

        return lessonReservationFlow.toList()
    }

    override suspend fun findById(id: LessonReservationId): LessonReservation? {
        val record = dslContext.get().nonBlockingFetchOne(
            LESSON_RESERVATIONS,
            LESSON_RESERVATIONS.ID.eq(id.value)
        ) ?: return null

        val dates = getLessonReservationDates(record.id!!)
        val educations = getLessonReservationEducations(record.id!!)
        val subjects = getLessonReservationSubjects(record.id!!)
        val grades = getLessonReservationGrades(record.id!!)

        return record.toEntity(dates, educations, subjects, grades)
    }

    private suspend fun getLessonReservationDates(lessonReservationId: Long): Nel<LessonReservationDate> {
        val dates = dslContext.get().nonBlockingFetch(
            LESSON_RESERVATION_DATES,
            LESSON_RESERVATION_DATES.LESSON_RESERVATION_ID.eq(lessonReservationId)
        ).map { record ->
            LessonReservationDate(
                date = record.startDate,
                startTime = record.startTime.toLocalTime() ?: LocalTime.of(9, 0),
                endTime = record.endTime.toLocalTime() ?: LocalTime.of(17, 0)
            )
        }

        return if (dates.isNotEmpty()) {
            nonEmptyListOf(dates.first(), *dates.drop(1).toTypedArray())
        } else {
            // Return a default date if no dates are found
            nonEmptyListOf(LessonReservationDate(
                date = LocalDate.now(),
                startTime = LocalTime.of(9, 0),
                endTime = LocalTime.of(17, 0)
            ))
        }
    }

    private suspend fun getLessonReservationEducations(lessonReservationId: Long): Set<EducationId> {
        return dslContext.get().nonBlockingFetch(
            LESSON_RESERVATION_EDUCATIONS,
            LESSON_RESERVATION_EDUCATIONS.LESSON_RESERVATION_ID.eq(lessonReservationId)
        ).map { EducationId(it.educationId) }.toSet()
    }

    private suspend fun getLessonReservationSubjects(lessonReservationId: Long): Set<Subject> {
        return dslContext.get().nonBlockingFetch(
            LESSON_RESERVATION_SUBJECTS,
            LESSON_RESERVATION_SUBJECTS.LESSON_RESERVATION_ID.eq(lessonReservationId)
        ).map { Subject.valueOf(it.subjectCode) }.toSet()
    }

    private suspend fun getLessonReservationGrades(lessonReservationId: Long): Set<Grade> {
        return dslContext.get().nonBlockingFetch(
            LESSON_RESERVATION_GRADES,
            LESSON_RESERVATION_GRADES.LESSON_RESERVATION_ID.eq(lessonReservationId)
        ).map { Grade.valueOf(it.gradeCode) }.toSet()
    }

    private fun LessonReservationsRecord.toEntity(
        dates: Nel<LessonReservationDate>,
        educations: Set<EducationId>,
        subjects: Set<Subject>,
        grades: Set<Grade>
    ): LessonReservation {
        return LessonReservation(
            id = LessonReservationId(id!!),
            lessonPlanId = LessonPlanId(lessonPlanId),
            reservedSchoolId = SchoolId(schoolId),
            reservedTeacherId = TeacherId(teacherId),
            educations = LessonReservationEducations(educations),
            subjects = LessonReservationSubjects(subjects),
            grades = LessonReservationGrades(grades),
            title = LessonReservationTitle(title ?: ""),
            description = LessonReservationDescription(description?: ""),
            lessonType = mapEnumToLessonType(lessonType),
            location = ReservationLessonLocation(location ?: ""),
            reservationDates = LessonReservationDates(dates)
        )
    }

    private fun mapEnumToLessonType(lessonType: LessonReservationsLessonType?): LessonType {
        return when (lessonType) {
         LessonReservationsLessonType.ONLINE -> LessonType.ONLINE
          LessonReservationsLessonType.OFFLINE -> LessonType.OFFLINE
         LessonReservationsLessonType.ONLINE_AND_OFFLINE -> LessonType.ONLINE_AND_OFFLINE
            null -> LessonType.OFFLINE
        }
    }

    // create
    @TestOnly
    override suspend fun create(lessonReservation: LessonReservation):LessonReservation {
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

package com.spotteacher.teacher.feature.lessonSchedule.infra

import com.spotteacher.infra.db.enums.LessonReservationsLessonType
import com.spotteacher.infra.db.enums.LessonSchedulesLessonType
import com.spotteacher.infra.db.enums.LessonSchedulesStatus
import com.spotteacher.infra.db.tables.LessonScheduleEducations.Companion.LESSON_SCHEDULE_EDUCATIONS
import com.spotteacher.infra.db.tables.LessonScheduleGrades.Companion.LESSON_SCHEDULE_GRADES
import com.spotteacher.infra.db.tables.LessonScheduleSubjects.Companion.LESSON_SCHEDULE_SUBJECTS
import com.spotteacher.infra.db.tables.LessonSchedules.Companion.LESSON_SCHEDULES
import com.spotteacher.infra.db.tables.records.LessonSchedulesRecord
import com.spotteacher.teacher.feature.lessonPlan.domain.LessonType
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservationDescription
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservationEducations
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservationGrades
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservationId
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservationSubjects
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservationTitle
import com.spotteacher.teacher.feature.lessonReservation.domain.ReservationLessonLocation
import com.spotteacher.teacher.feature.lessonSchedule.domain.DoingLessonSchedule
import com.spotteacher.teacher.feature.lessonSchedule.domain.LessonSchedule
import com.spotteacher.teacher.feature.lessonSchedule.domain.LessonScheduleId
import com.spotteacher.teacher.feature.lessonSchedule.domain.LessonScheduleRepository
import com.spotteacher.teacher.feature.lessonSchedule.domain.ScheduleDate
import com.spotteacher.teacher.feature.lessonTag.domain.EducationId
import com.spotteacher.teacher.feature.lessonTag.domain.Grade
import com.spotteacher.teacher.feature.lessonTag.domain.Subject
import com.spotteacher.teacher.feature.school.domain.SchoolId
import com.spotteacher.teacher.feature.teacher.domain.TeacherId
import com.spotteacher.teacher.shared.infra.TransactionAwareDSLContext
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitLast
import org.springframework.stereotype.Repository
import java.sql.Time
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Repository
class LessonScheduleRepositoryImpl(private val dslContext: TransactionAwareDSLContext) : LessonScheduleRepository {

    override suspend fun create(lessonSchedule: DoingLessonSchedule): DoingLessonSchedule {
        // Insert into lesson_schedules table
        val now = LocalDateTime.now()
        val lessonScheduleId = dslContext.get().insertInto(
            LESSON_SCHEDULES,
            LESSON_SCHEDULES.LESSON_RESERVATION_ID,
            LESSON_SCHEDULES.SCHOOL_ID,
            LESSON_SCHEDULES.TEACHER_ID,
            LESSON_SCHEDULES.LESSON_PLAN_ID,
            LESSON_SCHEDULES.STATUS,
            LESSON_SCHEDULES.TITLE,
            LESSON_SCHEDULES.DESCRIPTION,
            LESSON_SCHEDULES.LOCATION,
            LESSON_SCHEDULES.LESSON_TYPE,
            LESSON_SCHEDULES.DATE,
            LESSON_SCHEDULES.START_TIME,
            LESSON_SCHEDULES.END_TIME,
            LESSON_SCHEDULES.CREATED_AT,
            LESSON_SCHEDULES.UPDATED_AT
        ).values(
            lessonSchedule.lessonReservationId.value,
            lessonSchedule.reservedSchoolId.value,
            lessonSchedule.reservedTeacherId.value,
            1L, // Default value for lesson_plan_id
            LessonSchedulesStatus.DOING,
            lessonSchedule.title.value,
            lessonSchedule.description.value,
            lessonSchedule.location.value,
            mapLessonTypeToScheduleEnum(lessonSchedule.lessonType),
            lessonSchedule.scheduleDate.date,
            LocalDateTime.of(lessonSchedule.scheduleDate.date, lessonSchedule.scheduleDate.startTime),
            LocalDateTime.of(lessonSchedule.scheduleDate.date, LocalTime.parse(lessonSchedule.scheduleDate.endTime.toString())),
            now,
            now
        ).returning(LESSON_SCHEDULES.ID).awaitFirstOrNull()?.id!!

        // Insert educations
        val educationQueries = lessonSchedule.educations.value.map { educationId ->
            dslContext.get().insertInto(
                LESSON_SCHEDULE_EDUCATIONS,
                LESSON_SCHEDULE_EDUCATIONS.LESSON_SCHEDULE_ID,
                LESSON_SCHEDULE_EDUCATIONS.EDUCATION_ID,
                LESSON_SCHEDULE_EDUCATIONS.CREATED_AT,
                LESSON_SCHEDULE_EDUCATIONS.UPDATED_AT
            ).values(
                lessonScheduleId,
                educationId.value,
                now,
                now
            )
        }
        if (educationQueries.isNotEmpty()) {
            dslContext.get().batch(educationQueries).awaitLast()
        }

        // Insert subjects
        val subjectQueries = lessonSchedule.subjects.value.mapIndexed { index, subject ->
            dslContext.get().insertInto(
                LESSON_SCHEDULE_SUBJECTS,
                LESSON_SCHEDULE_SUBJECTS.LESSON_SCHEDULE_ID,
                LESSON_SCHEDULE_SUBJECTS.SUBJECT_CODE,
                LESSON_SCHEDULE_SUBJECTS.DISPLAY_ORDER
            ).values(
                lessonScheduleId,
                subject.name,
                index
            )
        }
        if (subjectQueries.isNotEmpty()) {
            dslContext.get().batch(subjectQueries).awaitLast()
        }

        // Insert grades
        val gradeQueries = lessonSchedule.grades.value.mapIndexed { index, grade ->
            dslContext.get().insertInto(
                LESSON_SCHEDULE_GRADES,
                LESSON_SCHEDULE_GRADES.LESSON_SCHEDULE_ID,
                LESSON_SCHEDULE_GRADES.GRADE_CODE,
                LESSON_SCHEDULE_GRADES.DISPLAY_ORDER
            ).values(
                lessonScheduleId,
                grade.name,
                index
            )
        }
        if (gradeQueries.isNotEmpty()) {
            dslContext.get().batch(gradeQueries).awaitLast()
        }

        return lessonSchedule.copy(id = LessonScheduleId(lessonScheduleId))
    }

    override suspend fun findById(id: LessonScheduleId): LessonSchedule? {
        val record = dslContext.get().selectFrom(LESSON_SCHEDULES)
            .where(LESSON_SCHEDULES.ID.eq(id.value))
            .awaitFirstOrNull() ?: return null

        // Get educations
        val educations = getLessonScheduleEducations(record.id)

        // Get subjects
        val subjects = getLessonScheduleSubjects(record.id)

        // Get grades
        val grades = getLessonScheduleGrades(record.id)

        return when (record.status) {
            LessonSchedulesStatus.DOING -> createDoingLessonSchedule(record, educations, subjects, grades)
            else -> null // For now, we only handle DOING status
        }
    }

    private suspend fun getLessonScheduleEducations(lessonScheduleId: Long?): Set<EducationId> {
        if (lessonScheduleId == null) return emptySet()

        return dslContext.get().select(LESSON_SCHEDULE_EDUCATIONS.EDUCATION_ID)
            .from(LESSON_SCHEDULE_EDUCATIONS)
            .where(LESSON_SCHEDULE_EDUCATIONS.LESSON_SCHEDULE_ID.eq(lessonScheduleId))
            .fetch()
            .map { EducationId(it.get(LESSON_SCHEDULE_EDUCATIONS.EDUCATION_ID) as Long) }
            .toSet()
    }

    private suspend fun getLessonScheduleSubjects(lessonScheduleId: Long?): Set<Subject> {
        if (lessonScheduleId == null) return emptySet()

        val subjectCodes = dslContext.get().select(LESSON_SCHEDULE_SUBJECTS.SUBJECT_CODE)
            .from(LESSON_SCHEDULE_SUBJECTS)
            .where(LESSON_SCHEDULE_SUBJECTS.LESSON_SCHEDULE_ID.eq(lessonScheduleId))
            .orderBy(LESSON_SCHEDULE_SUBJECTS.DISPLAY_ORDER.asc())
            .fetch()
            .map { it.get(LESSON_SCHEDULE_SUBJECTS.SUBJECT_CODE) }

        return subjectCodes.mapNotNull { code ->
            try {
                Subject.valueOf(code)
            } catch (e: IllegalArgumentException) {
                null
            }
        }.toSet()
    }

    private suspend fun getLessonScheduleGrades(lessonScheduleId: Long?): Set<Grade> {
        if (lessonScheduleId == null) return emptySet()

        val gradeCodes = dslContext.get().select(LESSON_SCHEDULE_GRADES.GRADE_CODE)
            .from(LESSON_SCHEDULE_GRADES)
            .where(LESSON_SCHEDULE_GRADES.LESSON_SCHEDULE_ID.eq(lessonScheduleId))
            .orderBy(LESSON_SCHEDULE_GRADES.DISPLAY_ORDER.asc())
            .fetch()
            .map { it.get(LESSON_SCHEDULE_GRADES.GRADE_CODE) }

        return gradeCodes.mapNotNull { code ->
            try {
                Grade.valueOf(code)
            } catch (e: IllegalArgumentException) {
                null
            }
        }.toSet()
    }

    private fun createDoingLessonSchedule(
        record: LessonSchedulesRecord,
        educations: Set<EducationId>,
        subjects: Set<Subject>,
        grades: Set<Grade>
    ): DoingLessonSchedule {
        return DoingLessonSchedule(
            id = LessonScheduleId(record.id ?: 0),
            lessonReservationId = LessonReservationId(record.lessonReservationId),
            reservedSchoolId = SchoolId(record.schoolId),
            reservedTeacherId = TeacherId(record.teacherId),
            scheduleDate = ScheduleDate(
                date = record.date,
                startTime = record.startTime.toLocalTime(),
                endTime = Time.valueOf(record.endTime.toLocalTime())
            ),
            educations = LessonReservationEducations(educations),
            subjects = LessonReservationSubjects(subjects),
            grades = LessonReservationGrades(grades),
            title = LessonReservationTitle(record.title),
            description = LessonReservationDescription(record.description ?: ""),
            lessonType = mapScheduleEnumToLessonType(record.lessonType),
            location = ReservationLessonLocation(record.location ?: "")
        )
    }

    private fun mapLessonTypeToReservationEnum(lessonType: LessonType): LessonReservationsLessonType {
        return when (lessonType) {
            LessonType.ONLINE -> LessonReservationsLessonType.ONLINE
            LessonType.OFFLINE -> LessonReservationsLessonType.OFFLINE
            LessonType.ONLINE_AND_OFFLINE -> LessonReservationsLessonType.ONLINE_AND_OFFLINE
        }
    }

    private fun mapLessonTypeToScheduleEnum(lessonType: LessonType): LessonSchedulesLessonType {
        return when (lessonType) {
            LessonType.ONLINE -> LessonSchedulesLessonType.ONLINE
            LessonType.OFFLINE -> LessonSchedulesLessonType.OFFLINE
            LessonType.ONLINE_AND_OFFLINE -> LessonSchedulesLessonType.ONLINE_AND_OFFLINE
        }
    }

    private fun mapScheduleEnumToLessonType(lessonType: LessonSchedulesLessonType?): LessonType {
        return when (lessonType) {
            LessonSchedulesLessonType.ONLINE -> LessonType.ONLINE
            LessonSchedulesLessonType.OFFLINE -> LessonType.OFFLINE
            LessonSchedulesLessonType.ONLINE_AND_OFFLINE -> LessonType.ONLINE_AND_OFFLINE
            null -> LessonType.ONLINE // Default to ONLINE if null
        }
    }
}

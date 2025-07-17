package com.spotteacher.admin.feature.lessonSchedule.handler

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.expediagroup.graphql.generator.annotations.GraphQLType
import com.expediagroup.graphql.generator.scalars.ID
import com.spotteacher.admin.feature.lessonPlan.domain.LessonType
import com.spotteacher.admin.feature.lessonSchedule.domain.LessonScheduleId
import com.spotteacher.admin.feature.lessonTag.domain.EducationId
import com.spotteacher.admin.feature.lessonTag.domain.Grade
import com.spotteacher.admin.feature.lessonTag.domain.Subject
import com.spotteacher.graphql.toID
import java.sql.Time
import java.time.LocalDate

private const val LESSON_SCHEDULE_TYPE_NAME = "LessonSchedule"

@GraphQLType(LESSON_SCHEDULE_TYPE_NAME)
data class LessonScheduleType(
    val id: ID,
    val lessonReservationId: ID,
    val reservedSchoolId: ID,
    val teacherId: ID,
    val lessonType: LessonType,
    val location: String,
    val title: String,
    val description: String,
    @GraphQLIgnore
    val educations: List<EducationId>,
    val subjects: List<Subject>,
    val grades: List<Grade>,
    val date: LocalDate,
    val startTime: Time,
    val endTime: Time,
)

fun LessonScheduleId.toGraphQLID() = this.toID(LESSON_SCHEDULE_TYPE_NAME)

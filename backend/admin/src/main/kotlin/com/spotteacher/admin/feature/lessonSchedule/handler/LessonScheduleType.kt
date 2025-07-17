package com.spotteacher.admin.feature.lessonSchedule.handler

import com.expediagroup.graphql.generator.annotations.GraphQLType
import com.expediagroup.graphql.generator.scalars.ID
import com.spotteacher.admin.feature.lessonPlan.domain.LessonType
import java.time.LocalDate
import java.time.LocalTime

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
    val educations: List<ID>,
    val subjects: List<ID>,
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
)
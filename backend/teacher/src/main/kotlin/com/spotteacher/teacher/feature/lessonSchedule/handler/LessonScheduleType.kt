package com.spotteacher.teacher.feature.lessonSchedule.handler

import com.expediagroup.graphql.generator.annotations.GraphQLType

private const val LESSON_SCHEDULE_TYPE = "LessonSchedule"

@GraphQLType(LESSON_SCHEDULE_TYPE)
data class LessonScheduleType(
    val id: String,
)

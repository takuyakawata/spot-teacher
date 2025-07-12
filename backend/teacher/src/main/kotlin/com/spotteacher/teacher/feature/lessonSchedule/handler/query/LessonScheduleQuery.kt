package com.spotteacher.teacher.feature.lessonSchedule.handler.query

import com.expediagroup.graphql.server.operations.Query
import com.spotteacher.teacher.feature.lessonSchedule.handler.LessonScheduleType
import org.springframework.stereotype.Component

@Component
class LessonScheduleQuery : Query {
    suspend fun lessonSchedule(): List<LessonScheduleType> {
        return emptyList()
    }
}

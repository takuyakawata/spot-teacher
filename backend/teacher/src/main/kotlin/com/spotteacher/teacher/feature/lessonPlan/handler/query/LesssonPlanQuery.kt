package com.spotteacher.teacher.feature.lessonPlan.handler.query

import com.expediagroup.graphql.server.operations.Query
import org.springframework.stereotype.Component

@Component
class LessonPlanQuery: Query {
    suspend fun lessonPlan(): String {
        return "Hello World"
    }

    suspend fun lessonPlanById(): String {
        return "Hello World"
    }
}

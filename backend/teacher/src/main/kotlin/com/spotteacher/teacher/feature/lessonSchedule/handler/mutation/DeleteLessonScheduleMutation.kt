package com.spotteacher.teacher.feature.lessonSchedule.handler.mutation

import com.expediagroup.graphql.server.operations.Mutation
import org.springframework.stereotype.Component

@Component
class DeleteLessonScheduleMutation: Mutation {
    suspend fun deleteLessonSchedule(): String {
        return "Hello World"
    }
}
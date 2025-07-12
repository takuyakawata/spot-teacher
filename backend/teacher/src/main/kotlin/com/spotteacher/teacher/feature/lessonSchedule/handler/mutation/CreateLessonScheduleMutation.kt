package com.spotteacher.teacher.feature.lessonSchedule.handler.mutation

import com.expediagroup.graphql.server.operations.Mutation
import org.springframework.stereotype.Component

@Component
class CreateLessonScheduleMutation: Mutation {
    suspend fun createLessonSchedule() {
        return Unit
    }
}

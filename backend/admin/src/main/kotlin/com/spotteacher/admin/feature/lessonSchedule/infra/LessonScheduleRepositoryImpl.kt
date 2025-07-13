package com.spotteacher.admin.feature.lessonSchedule.infra

import com.spotteacher.admin.feature.lessonSchedule.domain.DoingLessonSchedule
import com.spotteacher.admin.feature.lessonSchedule.domain.LessonSchedule
import com.spotteacher.admin.feature.lessonSchedule.domain.LessonScheduleId
import com.spotteacher.admin.feature.lessonSchedule.domain.LessonScheduleRepository
import org.springframework.stereotype.Repository

@Repository
class LessonScheduleRepositoryImpl : LessonScheduleRepository {
    override suspend fun create(lessonSchedule: DoingLessonSchedule): DoingLessonSchedule {
        // Return the input for now
        return lessonSchedule
    }

    override suspend fun findById(id: LessonScheduleId): LessonSchedule? {
        // Return null for now
        return null
    }
}
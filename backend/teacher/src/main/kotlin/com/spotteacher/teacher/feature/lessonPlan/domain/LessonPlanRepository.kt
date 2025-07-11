package com.spotteacher.teacher.feature.lessonPlan.domain

import com.spotteacher.domain.Pagination

interface LessonPlanRepository {
    suspend fun getPaginated(pagination: Pagination<LessonPlan>): List<LessonPlan>
    suspend fun findById(id: LessonPlanId): LessonPlan?
}

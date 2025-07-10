package com.spotteacher.teacher.feature.lessonPlan.domain

import com.spotteacher.domain.Pagination

interface LessonPlanRepository {
    suspend fun createDraft(lessonPlan: DraftLessonPlan): DraftLessonPlan
    suspend fun update(lessonPlan: LessonPlan)
    suspend fun updateStatus(lessonPlan: LessonPlan)
    suspend fun delete(id: LessonPlanId)
    suspend fun getAll(): List<LessonPlan>
    suspend fun getPaginated(pagination: Pagination<LessonPlan>): List<LessonPlan>
    suspend fun findById(id: LessonPlanId): LessonPlan?
}

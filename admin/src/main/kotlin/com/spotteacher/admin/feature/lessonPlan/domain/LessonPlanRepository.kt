package com.spotteacher.admin.feature.lessonPlan.domain

interface LessonPlanRepository {
    suspend fun createDraft(lessonPlan: DraftLessonPlan): DraftLessonPlan
    suspend fun update(lessonPlan: LessonPlan)
    suspend fun updateStatus(lessonPlan: LessonPlan)
    suspend fun delete(id: LessonPlanId)
    suspend fun getAll(): List<LessonPlan>
    suspend fun findById(id: LessonPlanId): LessonPlan?
}

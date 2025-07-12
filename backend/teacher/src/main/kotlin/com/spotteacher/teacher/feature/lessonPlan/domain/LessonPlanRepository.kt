package com.spotteacher.teacher.feature.lessonPlan.domain

import com.spotteacher.domain.Pagination
import org.jetbrains.annotations.TestOnly

interface LessonPlanRepository {
    suspend fun getPaginated(pagination: Pagination<LessonPlan>): List<LessonPlan>
    suspend fun findById(id: LessonPlanId): LessonPlan?

    @TestOnly
    suspend fun create(lessonPlan: LessonPlan): LessonPlan
}

package com.spotteacher.admin.feature.lessonPlan.usecase

import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlan
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanRepository
import com.spotteacher.domain.Pagination
import com.spotteacher.usecase.UseCase


@UseCase
class FindPaginatedLessonPlansUseCase(
    private val lessonPlanRepository: LessonPlanRepository
){
    suspend fun call(pagination: Pagination<LessonPlan>): List<LessonPlan> {
        return lessonPlanRepository.getPaginated(pagination)
    }
}

package com.spotteacher.teacher.feature.lessonPlan.usecase

import com.spotteacher.domain.ColumnValue
import com.spotteacher.domain.Pagination
import com.spotteacher.domain.SortOrder
import com.spotteacher.teacher.feature.lessonPlan.domain.LessonPlan
import com.spotteacher.teacher.feature.lessonPlan.domain.LessonPlanId
import com.spotteacher.teacher.feature.lessonPlan.domain.LessonPlanRepository
import com.spotteacher.usecase.UseCase
import java.time.LocalDateTime

@UseCase
class FindPaginatedLessonPlansUseCase(
    private val lessonPlanRepository: LessonPlanRepository
) {
    suspend fun call(
        limit: Int = DEFAULT_LIMIT,
        lastId: LessonPlanId? = null,
        lastCreatedAt: LocalDateTime? = null
    ): List<LessonPlan> {
        // If both lastId and lastCreatedAt are null, return the first page
        if (lastId == null || lastCreatedAt == null) {
            val pagination = Pagination<LessonPlan>(
                limit = limit
            )
            return lessonPlanRepository.getPaginated(pagination)
        }

        // Create pagination with cursor columns for id and createdAt
        val pagination = Pagination<LessonPlan>(
            limit = limit,
            ColumnValue(
                column = LessonPlan::createdAt,
                lastValue = lastCreatedAt,
                order = SortOrder.DESC,
                transform = { it }
            ),
            ColumnValue(
                column = LessonPlan::id,
                lastValue = lastId,
                order = SortOrder.DESC,
                transform = { it.value }
            )
        )

        return lessonPlanRepository.getPaginated(pagination)
    }

    companion object {
        const val DEFAULT_LIMIT = 10
    }
}

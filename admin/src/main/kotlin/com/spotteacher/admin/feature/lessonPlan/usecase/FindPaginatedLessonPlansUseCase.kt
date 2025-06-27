package com.spotteacher.admin.feature.lessonPlan.usecase

import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlan
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanId
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanRepository
import com.spotteacher.domain.ColumnValue
import com.spotteacher.domain.Pagination
import com.spotteacher.domain.SortOrder
import com.spotteacher.usecase.UseCase

data class FindPaginatedLessonPlansUseCaseInput(
    val lastId: Pair<LessonPlanId?, SortOrder>,
    val limit: Int
)

@UseCase
class FindPaginatedLessonPlansUseCase(
    private val lessonPlanRepository: LessonPlanRepository
) {
    suspend fun call(input: FindPaginatedLessonPlansUseCaseInput): List<LessonPlan> {
        return lessonPlanRepository.getPaginated(
            pagination = Pagination(
                limit = input.limit,
                cursorColumns = listOfNotNull(
                    ColumnValue(
                        column = LessonPlan::id,
                        lastValue = input.lastId.first?.value,
                        order = input.lastId.second,
                    ) { it }
                ).toTypedArray()
            )
        )
    }
}

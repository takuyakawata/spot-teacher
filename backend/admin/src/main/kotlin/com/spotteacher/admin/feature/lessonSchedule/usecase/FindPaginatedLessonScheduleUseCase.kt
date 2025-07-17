package com.spotteacher.admin.feature.lessonSchedule.usecase

import com.spotteacher.admin.feature.lessonSchedule.domain.LessonSchedule
import com.spotteacher.admin.feature.lessonSchedule.domain.LessonScheduleId
import com.spotteacher.admin.feature.lessonSchedule.domain.LessonScheduleRepository
import com.spotteacher.domain.ColumnValue
import com.spotteacher.domain.Pagination
import com.spotteacher.domain.SortOrder
import com.spotteacher.usecase.UseCase

data class FindPaginatedLessonScheduleUseCaseInput(
    val lastId: Pair<LessonScheduleId?, SortOrder>,
    val limit: Int
)

@UseCase
class FindPaginatedLessonScheduleUseCase(
    private val repository: LessonScheduleRepository
) {
    suspend fun call(input: FindPaginatedLessonScheduleUseCaseInput):List<LessonSchedule> = repository.getAll(
        pagination = Pagination(
            limit = input.limit,
            cursorColumns = listOfNotNull(
                ColumnValue(
                    column = LessonSchedule::id,
                    lastValue = input.lastId.first?.value,
                    order = input.lastId.second,
                ) { it }
            ).toTypedArray()
        )
    )
}
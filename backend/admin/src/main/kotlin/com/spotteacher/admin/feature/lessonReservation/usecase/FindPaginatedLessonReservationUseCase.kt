package com.spotteacher.admin.feature.lessonReservation.usecase

import com.spotteacher.admin.feature.lessonReservation.domain.LessonReservation
import com.spotteacher.admin.feature.lessonReservation.domain.LessonReservationId
import com.spotteacher.admin.feature.lessonReservation.domain.LessonReservationRepository
import com.spotteacher.domain.ColumnValue
import com.spotteacher.domain.Pagination
import com.spotteacher.domain.SortOrder
import com.spotteacher.usecase.UseCase

data class FindPaginatedLessonReservationUseCaseInput(
    val lastId: Pair<LessonReservationId?, SortOrder>,
    val limit: Int
)

@UseCase
class FindPaginatedLessonReservationUseCase(
    private val repository: LessonReservationRepository
) {
    suspend fun call(input: FindPaginatedLessonReservationUseCaseInput ):List<LessonReservation>{
        return repository.paginated(
            pagination = Pagination(
                limit = input.limit,
                cursorColumns = listOfNotNull(
                    ColumnValue(
                        column = LessonReservation::id,
                        lastValue = input.lastId.first?.value,
                        order = input.lastId.second,
                    ) { it }
                ).toTypedArray()
            )
        )
    }
}
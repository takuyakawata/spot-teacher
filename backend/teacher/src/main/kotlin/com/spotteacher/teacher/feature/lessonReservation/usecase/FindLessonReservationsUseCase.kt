package com.spotteacher.teacher.feature.lessonReservation.usecase

import com.spotteacher.domain.ColumnValue
import com.spotteacher.domain.Pagination
import com.spotteacher.domain.SortOrder
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservation
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservationId
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservationRepository
import com.spotteacher.teacher.feature.lessonReservation.domain.TeacherId
import com.spotteacher.teacher.feature.school.domain.SchoolId
import com.spotteacher.usecase.UseCase

data class FindLessonReservationsUseCaseInput(
    val teacherId: TeacherId,
    val schoolId: SchoolId,
    val lastId: Pair<LessonReservationId?, SortOrder>,
    val limit: Int
)

@UseCase
class FindLessonReservationsUseCase(
    private val lessonReservationRepository: LessonReservationRepository
) {
    suspend fun call(input: FindLessonReservationsUseCaseInput) :List<LessonReservation>{
        val pagination = Pagination(
            limit = input.limit,
            cursorColumns = listOfNotNull(
                ColumnValue(
                    column = LessonReservation::id,
                    lastValue = input.lastId.first?.value,
                    order = input.lastId.second,
                ) { it }
            ).toTypedArray()
        )

        return lessonReservationRepository.filterByTeacherAndSchoolId(
            teacherId = input.teacherId,
            schoolId = input.schoolId,
            pagination = pagination
        )
    }
}

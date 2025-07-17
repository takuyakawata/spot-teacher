package com.spotteacher.admin.feature.lessonSchedule.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.spotteacher.admin.feature.lessonSchedule.domain.LessonSchedule
import com.spotteacher.admin.feature.lessonSchedule.domain.LessonScheduleError
import com.spotteacher.admin.feature.lessonSchedule.domain.LessonScheduleErrorCode
import com.spotteacher.admin.feature.lessonSchedule.domain.LessonScheduleId
import com.spotteacher.admin.feature.lessonSchedule.domain.LessonScheduleRepository
import com.spotteacher.usecase.UseCase

@UseCase
class FindLessonScheduleUseCase(
    private val repository: LessonScheduleRepository
) {
    suspend fun call(id: LessonScheduleId): Either<LessonScheduleError, LessonSchedule> {
        val lessonSchedule = repository.findById(id)?:return LessonScheduleError(
            errorCode = LessonScheduleErrorCode.LESSON_SCHEDULE_NOT_FOUND,
            message = "Lesson schedule with ID ${id.value} not found"
        ).left()

        return lessonSchedule.right()
    }
}
package com.spotteacher.teacher.feature.lessonTag.usecase

import com.spotteacher.teacher.feature.lessonTag.domain.Education
import com.spotteacher.teacher.feature.lessonTag.domain.EducationRepository
import com.spotteacher.usecase.UseCase

@UseCase
class GetAllEducationsUseCase(
    private val educationRepository: EducationRepository
) {
    suspend fun call(): List<Education> = educationRepository.getAll()
}

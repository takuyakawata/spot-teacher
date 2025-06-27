package com.spotteacher.admin.feature.lessonTag.usecase

import com.spotteacher.admin.feature.lessonTag.domain.Education
import com.spotteacher.admin.feature.lessonTag.domain.EducationName
import com.spotteacher.admin.feature.lessonTag.domain.EducationRepository
import com.spotteacher.usecase.UseCase

@UseCase
class FindEducationsFilterByNameUseCase(
    private val educationRepository: EducationRepository
) {
    suspend fun call(name: EducationName): Education? = educationRepository.findByName(EducationName(name.value))
}

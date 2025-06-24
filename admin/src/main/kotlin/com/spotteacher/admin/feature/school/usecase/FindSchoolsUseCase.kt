package com.spotteacher.admin.feature.school.usecase

import arrow.core.Nel
import com.spotteacher.admin.feature.school.domain.School
import com.spotteacher.admin.feature.school.domain.SchoolRepository
import com.spotteacher.usecase.UseCase

data class FindSchoolsUseCaseOutput(
    val schools: Nel<School>
)

@UseCase
class FindSchoolsUseCase(
    private val schoolRepository: SchoolRepository
) {
    suspend fun call(): FindSchoolsUseCaseOutput {
        val schools = schoolRepository.getAll()
        return FindSchoolsUseCaseOutput(schools)
    }
}

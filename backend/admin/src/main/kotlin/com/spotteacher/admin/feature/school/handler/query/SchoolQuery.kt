package com.spotteacher.admin.feature.school.handler.query

import com.expediagroup.graphql.server.operations.Query
import com.spotteacher.admin.feature.school.domain.SchoolId
import com.spotteacher.admin.feature.school.handler.SchoolType
import com.spotteacher.admin.feature.school.handler.toSchoolType
import com.spotteacher.admin.feature.school.usecase.FindSchoolUseCase
import com.spotteacher.admin.feature.school.usecase.FindSchoolUseCaseInput
import com.spotteacher.admin.feature.school.usecase.FindSchoolsUseCase
import org.springframework.stereotype.Component

@Component
class SchoolQuery(
    private val findSchoolsUseCase: FindSchoolsUseCase,
    private val findSchoolUseCase: FindSchoolUseCase
) : Query {
    suspend fun schools(): List<SchoolType> {
        val output = findSchoolsUseCase.call()
        return output.schools.map { it.toSchoolType() }
    }

    suspend fun school(id: String): SchoolType? {
        val schoolId = SchoolId(id.toLongOrNull() ?: return null)
        val input = FindSchoolUseCaseInput(schoolId)
        val output = findSchoolUseCase.call(input)

        return output.result.fold(
            { null }, // Return null on error
            { it.toSchoolType() } // Convert school to SchoolType on success
        )
    }
}

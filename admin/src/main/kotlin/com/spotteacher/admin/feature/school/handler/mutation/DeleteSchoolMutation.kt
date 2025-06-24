package com.spotteacher.admin.feature.school.handler.mutation

import com.expediagroup.graphql.generator.annotations.GraphQLName
import com.expediagroup.graphql.server.operations.Mutation
import com.spotteacher.admin.feature.school.domain.SchoolId
import com.spotteacher.admin.feature.school.usecase.DeleteSchoolUseCase
import com.spotteacher.admin.feature.school.usecase.DeleteSchoolUseCaseInput
import org.springframework.stereotype.Component

@Component
class DeleteSchoolMutation(
    private val deleteSchoolUseCase: DeleteSchoolUseCase
) : Mutation {
    suspend fun deleteSchool(input: DeleteSchoolInput): DeleteSchoolPayload {
        // Convert input to domain objects
        val schoolId = SchoolId(input.id.toLongOrNull() ?: return DeleteSchoolPayload(
            success = false,
            errors = listOf("Invalid school ID format")
        ))

        // Create use case input
        val useCaseInput = DeleteSchoolUseCaseInput(schoolId)

        // Call use case
        val output = deleteSchoolUseCase.call(useCaseInput)

        // Handle result
        return output.result.fold(
            { error -> 
                DeleteSchoolPayload(
                    success = false,
                    errors = listOf(error.message)
                )
            },
            { 
                DeleteSchoolPayload(
                    success = true,
                    errors = emptyList()
                )
            }
        )
    }
}

@GraphQLName("DeleteSchoolInput")
data class DeleteSchoolInput(
    val id: String
)

@GraphQLName("DeleteSchoolPayload")
data class DeleteSchoolPayload(
    val success: Boolean,
    val errors: List<String>
)

package com.spotteacher.admin.feature.company.handler.mutation

import com.expediagroup.graphql.generator.scalars.ID
import com.expediagroup.graphql.server.operations.Mutation
import com.spotteacher.admin.feature.company.domain.CompanyErrorCode
import com.spotteacher.admin.feature.company.domain.CompanyId
import com.spotteacher.admin.feature.company.usecase.DeleteCompanyUseCase
import com.spotteacher.admin.feature.company.usecase.DeleteCompanyUseCaseInput
import com.spotteacher.graphql.toDomainId
import org.springframework.stereotype.Component

sealed interface DeleteCompanyMutationOutput
data class DeleteCompanyMutationSuccess(val result: Unit) : DeleteCompanyMutationOutput
data class DeleteCompanyMutationError(
    val message: String,
    val code: CompanyErrorCode
) : DeleteCompanyMutationOutput

@Component
class DeleteCompanyMutation(
    private val usecase: DeleteCompanyUseCase
) : Mutation {
    suspend fun deleteCompany(id: ID) : DeleteCompanyMutationOutput  {
        val result = usecase.call(
            DeleteCompanyUseCaseInput(
                id = id.toDomainId(::CompanyId)
            )
        ).result

        return result.fold(
            ifLeft = { error -> DeleteCompanyMutationError(error.message, error.code) },
            ifRight = { DeleteCompanyMutationSuccess(Unit)}
        )
    }
}

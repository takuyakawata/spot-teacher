package com.spotteacher.admin.feature.adminUser.handler.mutation

import com.expediagroup.graphql.generator.scalars.ID
import com.expediagroup.graphql.server.operations.Mutation
import com.spotteacher.admin.feature.adminUser.domain.AdminUserErrorCode
import com.spotteacher.admin.feature.adminUser.domain.AdminUserId
import com.spotteacher.admin.feature.adminUser.domain.Password
import com.spotteacher.admin.feature.adminUser.usecase.UpdatePasswordUseCase
import com.spotteacher.admin.feature.adminUser.usecase.UpdatePasswordUseCaseInput
import com.spotteacher.graphql.toDomainId
import org.springframework.stereotype.Component

data class UpdatePasswordMutationInput(
    val adminUserId: ID,
    val password: String,
    val confirmPassword: String,
)

sealed interface UpdatePassWordMutationOutput
data class UpdatePassWordMutationSuccess(val result: Unit) : UpdatePassWordMutationOutput
data class UpdatePassWordMutationError(
    val message: String,
    val code: AdminUserErrorCode
) : UpdatePassWordMutationOutput

@Component
class UpdatePasswordMutation(
    val usecase: UpdatePasswordUseCase
) : Mutation {
    suspend fun updatePassword(input: UpdatePasswordMutationInput) : UpdatePassWordMutationOutput {
        val result = usecase.call(
            UpdatePasswordUseCaseInput(
                adminUserId = input.adminUserId.toDomainId { AdminUserId(it) },
                password = Password(input.password),
                confirmPassword =  Password(input.confirmPassword)
            )
        )

        return result.fold(
            ifLeft = { error -> UpdatePassWordMutationError(error.message, error.code) },
            ifRight = { UpdatePassWordMutationSuccess(Unit) }
        )
    }
}



package com.spotteacher.admin.feature.adminUser.handler.mutation

import com.expediagroup.graphql.server.operations.Mutation
import com.spotteacher.admin.feature.adminUser.domain.AdminUserName
import com.spotteacher.admin.feature.adminUser.usecase.CreateAdminUserUseCase
import com.spotteacher.admin.feature.adminUser.usecase.CreateAdminUserUseCaseInput
import com.spotteacher.admin.shared.domain.Password
import com.spotteacher.domain.EmailAddress
import org.springframework.stereotype.Component

data class CreateAdminUserMutationInput(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val confirmPassword: String
)

@Component
class CreateAdminUserMutation(
    private val createAdminUserUseCase: CreateAdminUserUseCase
) : Mutation {
    suspend fun createAdminUser(input: CreateAdminUserMutationInput) {
        val useCaseInput = CreateAdminUserUseCaseInput(
            firstName = AdminUserName(input.firstName),
            lastName = AdminUserName(input.lastName),
            email = EmailAddress(input.email),
            password = Password(input.password),
            confirmPassword = Password(input.confirmPassword)
        )

        createAdminUserUseCase.call(useCaseInput)
    }
}

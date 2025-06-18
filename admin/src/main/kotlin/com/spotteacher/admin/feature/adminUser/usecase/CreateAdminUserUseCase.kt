package com.spotteacher.admin.feature.adminUser.usecase

import com.spotteacher.admin.feature.adminUser.domain.ActiveAdminUser
import com.spotteacher.admin.feature.adminUser.domain.AdminUser
import com.spotteacher.admin.feature.adminUser.domain.AdminUserName
import com.spotteacher.admin.feature.adminUser.domain.Password
import com.spotteacher.domain.EmailAddress
import com.spotteacher.usecase.UseCase

data class CreateAdminUserUseCaseInput(
    val firstName: AdminUserName,
    val lastName: AdminUserName,
    val email: EmailAddress,
    val password: Password,
    val confirm: Password,
)

sealed interface CreateAdminUserUseCaseOutput
data class CreateAdminUserUseCaseSuccess(val adminUser: AdminUser) : CreateAdminUserUseCaseOutput
data class CreateAdminUserUseCaseError(val message: String) : CreateAdminUserUseCaseOutput

@UseCase
class CreateAdminUserUseCase {
    fun execute(input: CreateAdminUserUseCaseInput): CreateAdminUserUseCaseOutput {
        // Validate password confirmation
        if (input.password.value != input.confirm.value) {
            return CreateAdminUserUseCaseError("Password and confirmation do not match")
        }

        // Create admin user
        val adminUser = ActiveAdminUser.create(
            firstName = input.firstName,
            lastName = input.lastName,
            email = input.email,
            password = input.password
        )

        return CreateAdminUserUseCaseSuccess(adminUser)
    }
}

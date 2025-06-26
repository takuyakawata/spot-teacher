package com.spotteacher.admin.feature.adminUser.usecase

import com.spotteacher.admin.feature.adminUser.domain.ActiveAdminUser
import com.spotteacher.admin.feature.adminUser.domain.AdminUser
import com.spotteacher.admin.feature.adminUser.domain.AdminUserName
import com.spotteacher.admin.feature.adminUser.domain.AdminUserRepository
import com.spotteacher.admin.shared.domain.Password
import com.spotteacher.admin.shared.infra.TransactionCoroutine
import com.spotteacher.domain.EmailAddress
import com.spotteacher.usecase.UseCase
import org.springframework.security.crypto.password.PasswordEncoder

data class CreateAdminUserUseCaseInput(
    val firstName: AdminUserName,
    val lastName: AdminUserName,
    val email: EmailAddress,
    val password: Password,
    val confirmPassword: Password,
)

sealed interface CreateAdminUserUseCaseOutput
data class CreateAdminUserUseCaseSuccess(val adminUser: AdminUser) : CreateAdminUserUseCaseOutput
data class CreateAdminUserUseCaseError(val message: String) : CreateAdminUserUseCaseOutput

@UseCase
class CreateAdminUserUseCase(
    private val adminUserRepository: AdminUserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    @TransactionCoroutine
    suspend fun call(input: CreateAdminUserUseCaseInput): CreateAdminUserUseCaseOutput {
        // Validate password confirmation
        if (input.password.value != input.confirmPassword.value) {
            return CreateAdminUserUseCaseError("Password and confirmation do not match")
        }

        val hashedPassword = passwordEncoder.encode(input.password.value)

        // Create admin user
        val adminUser = ActiveAdminUser.create(
            firstName = input.firstName,
            lastName = input.lastName,
            email = input.email,
        )

        adminUserRepository.create(adminUser, hashedPassword)

        return CreateAdminUserUseCaseSuccess(adminUser)
    }
}

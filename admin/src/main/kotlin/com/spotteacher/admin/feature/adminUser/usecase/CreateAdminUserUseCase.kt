package com.spotteacher.admin.feature.adminUser.usecase

import com.spotteacher.admin.feature.adminUser.domain.ActiveAdminUser
import com.spotteacher.admin.feature.adminUser.domain.AdminUser
import com.spotteacher.admin.feature.adminUser.domain.AdminUserCreator
import com.spotteacher.admin.feature.adminUser.domain.AdminUserError
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

@UseCase
class CreateAdminUserUseCase(
    private val adminUserCreator: AdminUserCreator
) {
    @TransactionCoroutine
    suspend fun call(input: CreateAdminUserUseCaseInput): Result<Unit>{
        require(input.password.value == input.confirmPassword.value){"Password and confirmation do not match"}

        return runCatching {
            val adminUser = ActiveAdminUser.create(
                firstName = input.firstName,
                lastName = input.lastName,
                email = input.email,
            )

            adminUserCreator.createAdminUser(adminUser, input.password)
        }
    }
}

package com.spotteacher.admin.feature.adminUser.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.spotteacher.admin.feature.adminUser.domain.ActiveAdminUser
import com.spotteacher.admin.feature.adminUser.domain.AdminUserError
import com.spotteacher.admin.feature.adminUser.domain.AdminUserErrorCode
import com.spotteacher.admin.feature.adminUser.domain.AdminUserId
import com.spotteacher.admin.feature.adminUser.domain.AdminUserRepository
import com.spotteacher.admin.feature.adminUser.domain.changePassword
import com.spotteacher.admin.shared.domain.Password
import com.spotteacher.usecase.UseCase


data class UpdatePasswordUseCaseInput(
    val adminUserId: AdminUserId,
    val password: Password,
    val confirmPassword: Password,
)

@UseCase
class UpdatePasswordUseCase(
    private val adminUserRepository: AdminUserRepository,
) {
    suspend fun call(input: UpdatePasswordUseCaseInput ): Either<AdminUserError, Unit> {

        require(input.password == input.confirmPassword) {"Password and confirmation do not match"}

        val adminUser = adminUserRepository.findById(input.adminUserId) ?:return AdminUserError(
            code = AdminUserErrorCode.ADMIN_USER_NOT_FOUND,
            message = "Admin user not found"
        ).left()

        require(adminUser is ActiveAdminUser) { return AdminUserError(
            code = AdminUserErrorCode.ADMIN_USER_NOT_FOUND,
            message = "Admin user not found"
        ).left()}

        val updatedPasswordAdminUser = adminUser.changePassword()

        adminUserRepository.updatePassword(input.password)
        return Unit.right()
    }
}

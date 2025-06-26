package com.spotteacher.admin.feature.adminUser.usecase

import com.spotteacher.admin.feature.adminUser.domain.AdminUserId
import com.spotteacher.admin.feature.adminUser.domain.AdminUserName
import com.spotteacher.admin.shared.domain.Password
import com.spotteacher.domain.EmailAddress
import com.spotteacher.usecase.UseCase

data class UpdateAdminUserUseCaseInput(
    val adminUserId: AdminUserId,
    val firstName: AdminUserName?,
    val lastName: AdminUserName?,
    val email: EmailAddress?,
    val password: Password,
    val confirm: Password,
)

@UseCase
class UpdateAdminUserUseCase

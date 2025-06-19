package com.spotteacher.admin.feature.adminUser.usecase

import com.spotteacher.admin.feature.adminUser.domain.AdminUserId

data class DeleteAdminUserUseCaseInput(
    val adminUserId: AdminUserId
)

//TODO 優先度　低　
class DeleteAdminUserUseCase

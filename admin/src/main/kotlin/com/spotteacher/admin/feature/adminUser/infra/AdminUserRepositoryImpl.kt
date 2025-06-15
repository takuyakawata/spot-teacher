package com.spotteacher.admin.feature.adminUser.infra

import com.spotteacher.admin.feature.adminUser.domain.AdminUser
import com.spotteacher.admin.feature.adminUser.domain.AdminUserId
import com.spotteacher.admin.feature.adminUser.domain.AdminUserRepository
import org.springframework.stereotype.Component

@Component
class AdminUserRepositoryImpl : AdminUserRepository {
    override suspend fun getAllUsers(): List<AdminUser> {
        TODO("Not yet implemented")
    }

    override suspend fun getUserById(id: AdminUserId): AdminUser? {
        TODO("Not yet implemented")
    }

    override suspend fun create(user: AdminUser) {
        TODO("Not yet implemented")
    }

    override suspend fun update(user: AdminUser) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(id: AdminUserId) {
        TODO("Not yet implemented")
    }
}

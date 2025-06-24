package com.spotteacher.admin.feature.adminUser.domain

import com.spotteacher.admin.shared.domain.Password
import com.spotteacher.domain.EmailAddress

interface AdminUserRepository {
    suspend fun getAll(): List<AdminUser>
    suspend fun findById(id: AdminUserId): AdminUser?
    suspend fun create(user: ActiveAdminUser,password: String): ActiveAdminUser
    suspend fun update(user: ActiveAdminUser)
    suspend fun updatePassword(password: Password)
    suspend fun delete(id: AdminUserId)
    suspend fun findByEmailAndActiveUser(emailAddress: EmailAddress): ActiveAdminUser?
}

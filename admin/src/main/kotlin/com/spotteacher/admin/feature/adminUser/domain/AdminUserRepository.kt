package com.spotteacher.admin.feature.adminUser.domain

import com.spotteacher.domain.EmailAddress

interface AdminUserRepository {
    suspend fun getAll(): List<AdminUser>
    suspend fun findById(id: AdminUserId): AdminUser?
    suspend fun create(user: AdminUser)
    suspend fun update(user: AdminUser)
    suspend fun delete(id: AdminUserId)
    suspend fun findByEmailAndActiveUser(emailAddress: EmailAddress):ActiveAdminUser?
}

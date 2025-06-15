package com.spotteacher.admin.feature.adminUser.domain

interface AdminUserRepository {
    suspend fun getAllUsers(): List<AdminUser>
    suspend fun getUserById(id: AdminUserId): AdminUser?
    suspend fun create(user: AdminUser)
    suspend fun update(user: AdminUser)
    suspend fun delete(id: AdminUserId)
}

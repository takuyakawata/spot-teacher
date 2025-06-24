package com.spotteacher.admin.fixture

import com.spotteacher.admin.feature.adminUser.domain.ActiveAdminUser
import com.spotteacher.admin.feature.adminUser.domain.AdminUserId
import com.spotteacher.admin.feature.adminUser.domain.AdminUserName
import com.spotteacher.admin.feature.adminUser.domain.AdminUserRepository
import com.spotteacher.admin.shared.domain.Password
import com.spotteacher.domain.EmailAddress
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class AdminUserFixture {

    @Autowired
    private lateinit var  repository: AdminUserRepository

    private var adminUserIdCount = 1L

    fun buildActiveAdminUser(
        id: AdminUserId = AdminUserId(adminUserIdCount++),
        firstName: AdminUserName = AdminUserName("Test"),
        lastName: AdminUserName = AdminUserName("User"),
        email: EmailAddress = EmailAddress("test@example.com"),
    ): ActiveAdminUser {
        return ActiveAdminUser(
            id = id,
            firstName = firstName,
            lastName = lastName,
            email = email,
        )
    }

    suspend fun createActiveAdminUser(
        firstName: AdminUserName = AdminUserName("Test"),
        lastName: AdminUserName = AdminUserName("User"),
        email: EmailAddress = EmailAddress("test@example.com"),
        password:Password = Password("password123")
    ): ActiveAdminUser{
        val adminUser = buildActiveAdminUser(
            firstName = firstName,
            lastName = lastName,
            email = email,
        )

        repository.create(adminUser, Password("pass1234").value)
        return adminUser
    }
}


package com.spotteacher.admin.fixture

import com.spotteacher.admin.feature.adminUser.domain.ActiveAdminUser
import com.spotteacher.admin.feature.adminUser.domain.AdminUser
import com.spotteacher.admin.feature.adminUser.domain.AdminUserId
import com.spotteacher.admin.feature.adminUser.domain.AdminUserName
import com.spotteacher.admin.feature.adminUser.domain.Password
import com.spotteacher.domain.EmailAddress
import org.springframework.stereotype.Component

@Component
class AdminUserFixture {
    private var adminUserIdCount = 1L

    fun buildActiveAdminUser(
        id: AdminUserId = AdminUserId(adminUserIdCount++),
        firstName: AdminUserName = AdminUserName("Test"),
        lastName: AdminUserName = AdminUserName("User"),
        email: EmailAddress = EmailAddress("test@example.com"),
        password: Password = Password("password123")
    ): ActiveAdminUser {
        return ActiveAdminUser(
            id = id,
            firstName = firstName,
            lastName = lastName,
            email = email,
            password = password
        )
    }
}

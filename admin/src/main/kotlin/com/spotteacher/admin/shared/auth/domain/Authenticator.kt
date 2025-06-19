package com.spotteacher.admin.shared.auth.domain

import com.spotteacher.admin.feature.adminUser.domain.ActiveAdminUser
import com.spotteacher.admin.feature.adminUser.domain.Password
import com.spotteacher.domain.EmailAddress

interface Authenticator {
    suspend fun authenticate(email: EmailAddress, rawPassword: Password): ActiveAdminUser
}

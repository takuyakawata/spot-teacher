package com.spotteacher.admin.shared.auth.refreshToken.domain

import com.spotteacher.admin.feature.adminUser.domain.Password
import com.spotteacher.domain.EmailAddress

interface Authenticator {
    suspend fun authenticate(email: EmailAddress, rawPassword: Password)
}

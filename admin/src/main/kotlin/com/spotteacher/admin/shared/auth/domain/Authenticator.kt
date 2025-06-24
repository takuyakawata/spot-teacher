package com.spotteacher.admin.shared.auth.domain

import com.spotteacher.admin.shared.domain.Password
import com.spotteacher.domain.EmailAddress

interface Authenticator {
    suspend fun authenticate(email: EmailAddress, password: Password): AuthUser
}

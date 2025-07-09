package com.spotteacher.teacher.shared.auth.domain

import com.spotteacher.domain.EmailAddress

interface Authenticator {
    suspend fun authenticate(email: EmailAddress, password: String): AuthUser
}

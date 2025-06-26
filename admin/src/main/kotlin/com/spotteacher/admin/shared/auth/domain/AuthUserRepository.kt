package com.spotteacher.admin.shared.auth.domain

import com.spotteacher.domain.EmailAddress

interface AuthUserRepository {
    suspend fun findByEmail(email: EmailAddress): AuthUser?
}

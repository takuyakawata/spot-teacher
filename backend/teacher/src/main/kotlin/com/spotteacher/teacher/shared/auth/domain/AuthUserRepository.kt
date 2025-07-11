package com.spotteacher.teacher.shared.auth.domain

import com.spotteacher.domain.EmailAddress

interface AuthUserRepository {
    suspend fun findByEmail(email: EmailAddress): AuthUser?
    suspend fun findByEmailAndActiveUser(email: EmailAddress): AuthUser?
}

package com.spotteacher.teacher.shared.auth.domain

import com.spotteacher.domain.EmailAddress

interface RefreshTokenManager {
    suspend fun createAndSaveRefreshToken(email: EmailAddress): RefreshToken
}

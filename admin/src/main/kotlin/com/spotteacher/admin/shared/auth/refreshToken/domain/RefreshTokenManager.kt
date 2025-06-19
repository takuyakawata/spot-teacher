package com.spotteacher.admin.shared.auth.refreshToken.domain

import com.spotteacher.domain.EmailAddress

interface RefreshTokenManager {
    suspend fun createAndSaveRefreshToken(email: EmailAddress): RefreshToken
}

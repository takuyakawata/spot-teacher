package com.spotteacher.admin.shared.auth.refreshToken.domain

interface RefreshTokenRepository {
    suspend fun save(refreshToken: RefreshToken): RefreshToken
}

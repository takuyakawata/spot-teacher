package com.spotteacher.admin.shared.auth.domain

interface RefreshTokenRepository {
    suspend fun save(refreshToken: RefreshToken): RefreshToken
}

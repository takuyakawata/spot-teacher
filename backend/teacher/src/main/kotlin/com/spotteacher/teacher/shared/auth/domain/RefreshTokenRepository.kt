package com.spotteacher.teacher.shared.auth.domain

interface RefreshTokenRepository {
    suspend fun save(refreshToken: RefreshToken): RefreshToken
}

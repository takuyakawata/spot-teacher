package com.spotteacher.teacher.shared.auth.domain

import com.spotteacher.teacher.shared.auth.domain.AuthUserId
import com.spotteacher.util.Identity
import java.time.LocalDateTime

data class TokenPair(
    val accessToken: String,
    val refreshToken: String // todo RefreshTokenにする
)

data class RefreshToken(
    val id: RefreshTokenId,
    val userId: AuthUserId,
    val token: String,
    val expiresAt: LocalDateTime,
) {
    companion object {
        fun create(
            userId: AuthUserId,
            token: String,
            expiresAt: LocalDateTime,
        ) = RefreshToken(
            id = RefreshTokenId(0),
            userId = userId,
            token = token,
            expiresAt = expiresAt
        )
    }
}

class RefreshTokenId(override val value: Long) : Identity<Long>(value)

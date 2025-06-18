package com.spotteacher.admin.shared.auth.refreshToken.domain

import com.spotteacher.admin.feature.adminUser.domain.AdminUserId
import com.spotteacher.util.Identity
import java.time.LocalDateTime


data class RefreshToken(
    val id : RefreshTokenId,
    val userId: AdminUserId,
    val token: String,
    val expiresAt: LocalDateTime,
){
    companion object{
        fun create(
            adminUserId: AdminUserId,
            token: String,
            expiresAt: LocalDateTime,
        ) = RefreshToken(
            id = RefreshTokenId(0),
            userId = adminUserId,
            token = token,
            expiresAt = expiresAt
        )
    }
}



class RefreshTokenId(override val value: Long) : Identity<Long>(value)

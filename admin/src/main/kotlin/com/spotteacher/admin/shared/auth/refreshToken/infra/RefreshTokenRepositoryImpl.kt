package com.spotteacher.admin.shared.auth.refreshToken.infra

import com.spotteacher.admin.shared.auth.refreshToken.domain.RefreshToken
import com.spotteacher.admin.shared.auth.refreshToken.domain.RefreshTokenRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository

@Component
class RefreshTokenRepositoryImpl : RefreshTokenRepository {
    override suspend fun save(refreshToken: RefreshToken): RefreshToken {
        return refreshToken
    }
}

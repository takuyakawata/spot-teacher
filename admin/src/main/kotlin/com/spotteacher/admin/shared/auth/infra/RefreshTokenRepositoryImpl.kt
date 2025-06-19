package com.spotteacher.admin.shared.auth.infra

import com.spotteacher.admin.shared.auth.domain.RefreshToken
import com.spotteacher.admin.shared.auth.domain.RefreshTokenRepository
import org.springframework.stereotype.Component

@Component
class RefreshTokenRepositoryImpl : RefreshTokenRepository {
    override suspend fun save(refreshToken: RefreshToken): RefreshToken {
        return refreshToken
    }
}

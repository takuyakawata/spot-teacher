package com.spotteacher.admin.shared.auth.infra

import com.spotteacher.admin.shared.auth.domain.RefreshToken
import com.spotteacher.admin.shared.auth.domain.RefreshTokenId
import com.spotteacher.admin.shared.auth.domain.RefreshTokenRepository
import com.spotteacher.admin.shared.infra.TransactionAwareDSLContext
import com.spotteacher.infra.db.tables.RefreshTokens.Companion.REFRESH_TOKENS
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.springframework.stereotype.Repository

@Repository
class RefreshTokenRepositoryImpl(
    private val dslContext: TransactionAwareDSLContext
) : RefreshTokenRepository {
    override suspend fun save(refreshToken: RefreshToken): RefreshToken {
        val id = dslContext.get().insertInto(
            REFRESH_TOKENS,
            REFRESH_TOKENS.TOKEN,
            REFRESH_TOKENS.USER_ID,
            REFRESH_TOKENS.EXPIRES_AT
        ).values(
            refreshToken.token,
            refreshToken.userId.value,
            refreshToken.expiresAt,
        ).returning(REFRESH_TOKENS.ID).awaitFirstOrNull()?.id!!

        return refreshToken.copy(id = RefreshTokenId(id))
    }
}

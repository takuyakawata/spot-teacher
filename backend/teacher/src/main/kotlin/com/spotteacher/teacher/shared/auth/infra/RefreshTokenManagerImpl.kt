package com.spotteacher.teacher.shared.auth.infra

import com.spotteacher.domain.EmailAddress
import com.spotteacher.exception.ResourceNotFoundException
import com.spotteacher.teacher.shared.auth.domain.RefreshToken
import com.spotteacher.teacher.shared.auth.domain.RefreshTokenManager
import com.spotteacher.teacher.shared.auth.domain.RefreshTokenRepository
import com.spotteacher.teacher.shared.auth.domain.AuthUser
import com.spotteacher.teacher.shared.auth.domain.AuthUserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

@Component
class RefreshTokenManagerImpl(
    private val refreshTokenRepository: RefreshTokenRepository,
    private val userRepository: AuthUserRepository,
    @Value("\${jwt.refresh-token-expiration-ms}")
    val refreshTokenExpirationMs: Long
) : RefreshTokenManager {
    override suspend fun createAndSaveRefreshToken(email: EmailAddress): RefreshToken {
        val authUser = userRepository.findByEmailAndActiveUser(email) ?: throw ResourceNotFoundException(
            clazz = AuthUser::class,
            params = mapOf("email" to email.value)
        )

        val newRefreshToken = RefreshToken.create(
            userId = authUser.id,
            token = UUID.randomUUID().toString(),
            expiresAt =  LocalDateTime.now().plusNanos(refreshTokenExpirationMs)
        )

        refreshTokenRepository.save(newRefreshToken)
        return newRefreshToken
    }
}

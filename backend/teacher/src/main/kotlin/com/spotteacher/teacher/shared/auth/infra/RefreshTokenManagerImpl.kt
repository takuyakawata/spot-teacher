package com.spotteacher.teacher.shared.auth.infra

import com.spotteacher.domain.EmailAddress
import com.spotteacher.exception.ResourceNotFoundException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

@Component
class RefreshTokenManagerImpl(
    private val refreshTokenRepository: RefreshTokenRepository,
    private val userRepository: AdminUserRepository,
    @Value("\${jwt.refresh-token-expiration-ms}")
    val refreshTokenExpirationMs: Long
) : RefreshTokenManager {
    override suspend fun createAndSaveRefreshToken(email: EmailAddress): RefreshToken {
        val adminUser = userRepository.findByEmailAndActiveUser(email) ?: throw ResourceNotFoundException(
            clazz = AdminUser::class,
            params = mapOf("email" to email.value)
        )

        val newRefreshToken = RefreshToken.create(
            adminUserId = adminUser.id,
            token = UUID.randomUUID().toString(),
            expiresAt =  LocalDateTime.now().plusNanos(refreshTokenExpirationMs)
        )

        refreshTokenRepository.save(newRefreshToken)
        return newRefreshToken
    }
}

package com.spotteacher.admin.feature.adminUser.domain

import com.spotteacher.admin.shared.auth.domain.AuthUser
import com.spotteacher.admin.shared.auth.domain.AuthUserRepository
import com.spotteacher.admin.shared.domain.Password
import com.spotteacher.exception.ResourceNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AdminUserCreator(
    private val adminUserRepository: AdminUserRepository,
    private val authUserRepository: AuthUserRepository,
    private val passwordEncoder: PasswordEncoder,
){
    suspend fun createAdminUser(adminUser: ActiveAdminUser, password: Password){
        adminUserRepository.findByEmail(adminUser.email)?:throw ResourceNotFoundException(
            clazz = AdminUser::class,
            params = mapOf("email" to adminUser.email.value)
        )

        authUserRepository.findByEmail(adminUser.email)?:throw ResourceNotFoundException(
            clazz = AuthUser::class,
            params = mapOf("email" to adminUser.email.value)
        )

        val hashedPassword = passwordEncoder.encode(password.value)
        adminUserRepository.create(adminUser, hashedPassword)
    }
}

package com.spotteacher.admin.feature.adminUser.domain

import com.spotteacher.admin.shared.auth.domain.AuthUserRepository
import com.spotteacher.admin.shared.domain.Password
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AdminUserCreator(
    private val adminUserRepository: AdminUserRepository,
    private val authUserRepository: AuthUserRepository,
    private val passwordEncoder: PasswordEncoder,
){
    suspend fun createAdminUser(adminUser: ActiveAdminUser, password: Password){
        val exitAuthUser = authUserRepository.findByEmail(adminUser.email)
        require(exitAuthUser == null){"User does not exist in auth service"}

        val exitAdminUser = adminUserRepository.findByEmail(adminUser.email)
        require(exitAdminUser == null){"User already exists in admin service"}

        val hashedPassword = passwordEncoder.encode(password.value)
        adminUserRepository.create(adminUser, hashedPassword)
    }
}

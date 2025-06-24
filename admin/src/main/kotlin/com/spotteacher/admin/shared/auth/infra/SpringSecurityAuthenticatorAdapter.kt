package com.spotteacher.admin.shared.auth.infra

import com.spotteacher.admin.feature.adminUser.domain.ActiveAdminUser
import com.spotteacher.admin.feature.adminUser.domain.AdminUser
import com.spotteacher.admin.feature.adminUser.domain.AdminUserRepository
import com.spotteacher.admin.feature.adminUser.domain.Password
import com.spotteacher.admin.shared.auth.domain.Authenticator
import com.spotteacher.domain.EmailAddress
import com.spotteacher.exception.ResourceNotFoundException
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Component

@Component // インフラ層のSpringコンポーネント
class SpringSecurityAuthenticatorAdapter(
    private val authenticationManager: ReactiveAuthenticationManager,
    private val userRepository: AdminUserRepository
) : Authenticator {
    override suspend fun authenticate(email: EmailAddress, password: Password): ActiveAdminUser {
        val authenticationToken = UsernamePasswordAuthenticationToken(email.value, password.value)

        authenticationManager.authenticate(authenticationToken).awaitSingleOrNull()
            ?: throw BadCredentialsException("Authentication failed: Bad credentials")
        val adminUser = userRepository.findByEmailAndActiveUser(email)
            ?: throw ResourceNotFoundException(
                clazz = AdminUser::class,
                params = mapOf("email" to email.value)
            )

        return adminUser
    }
}

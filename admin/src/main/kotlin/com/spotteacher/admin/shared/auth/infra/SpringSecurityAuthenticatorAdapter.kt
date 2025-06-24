package com.spotteacher.admin.shared.auth.infra

import com.spotteacher.admin.feature.adminUser.domain.AdminUserRepository
import com.spotteacher.admin.shared.auth.domain.AuthUser
import com.spotteacher.admin.shared.auth.domain.AuthUserRepository
import com.spotteacher.admin.shared.auth.domain.Authenticator
import com.spotteacher.admin.shared.domain.Password
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
    private val userRepository: AdminUserRepository,
    private val authUserRepository: AuthUserRepository
) : Authenticator {
    override suspend fun authenticate(email: EmailAddress, password: Password): AuthUser {
        val authenticationToken = UsernamePasswordAuthenticationToken(email.value, password.value)

        authenticationManager.authenticate(authenticationToken).awaitSingleOrNull()
            ?: throw BadCredentialsException("Authentication failed: Bad credentials")

      val authUser = authUserRepository.findByEmail(email) ?: throw ResourceNotFoundException(
          clazz = AuthUser::class,
          params = mapOf("email" to email.value)
      )

        return authUser
    }
}

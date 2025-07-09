package com.spotteacher.teacher.shared.auth.infra

import com.spotteacher.admin.shared.auth.domain.AuthUser
import com.spotteacher.admin.shared.auth.domain.Authenticator
import com.spotteacher.admin.shared.domain.Password
import com.spotteacher.domain.EmailAddress
import com.spotteacher.exception.AuthenticationFailedException
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.stereotype.Component

@Component
class SpringSecurityAuthenticatorAdapter(
    private val authenticationManager: ReactiveAuthenticationManager
) : Authenticator {
    override suspend fun authenticate(email: EmailAddress, password: String): AuthUser {
        try {
            val authenticationToken = UsernamePasswordAuthenticationToken(email.value, password)
            val authenticated = authenticationManager.authenticate(authenticationToken).awaitSingle()
            val userDetails = authenticated.principal as org.springframework.security.core.userdetails.User

            return AuthUser(
                email = EmailAddress(userDetails.username),
                password = Password(password)
            )
        } catch (e: BadCredentialsException) {
            throw AuthenticationFailedException("Invalid credentials")
        } catch (e: AuthenticationException) {
            throw AuthenticationFailedException(e.message ?: "Authentication failed")
        }
    }
}

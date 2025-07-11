package com.spotteacher.teacher.shared.auth.infra


import com.spotteacher.domain.EmailAddress
import com.spotteacher.exception.AuthenticationFailedException
import com.spotteacher.teacher.shared.auth.domain.AuthUser
import com.spotteacher.teacher.shared.auth.domain.Authenticator
import com.spotteacher.teacher.shared.domain.Password
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component

@Component
class SpringSecurityAuthenticatorAdapter(
    private val authenticationManager: ReactiveAuthenticationManager
) : Authenticator {
    override suspend fun authenticate(email: EmailAddress, password: String): AuthUser {
        try {
            val authenticationToken = UsernamePasswordAuthenticationToken(email.value, password)
            val authenticated = authenticationManager.authenticate(authenticationToken).awaitSingle()
            val userDetails = authenticated.principal as User

            return AuthUser(
                email = EmailAddress(userDetails.username),
                password = Password(password),
            )
        } catch (e: BadCredentialsException) {
            throw AuthenticationFailedException("Invalid credentials")
        } catch (e: AuthenticationException) {
            throw AuthenticationFailedException(e.message ?: "Authentication failed")
        }
    }
}

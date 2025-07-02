package com.spotteacher.admin.shared.auth.infra

import com.spotteacher.admin.shared.auth.domain.AuthUserRepository
import com.spotteacher.domain.EmailAddress
import kotlinx.coroutines.reactor.mono
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class UserDetailsServiceImpl(
    private val authUserRepository: AuthUserRepository
) : ReactiveUserDetailsService {
    override fun findByUsername(username: String): Mono<UserDetails> {
        return mono {
            val authAdminUser =  authUserRepository.findByEmail(EmailAddress(username))
                ?: throw UsernameNotFoundException("User not found")

            val authorities = listOf(SimpleGrantedAuthority("ADMIN"))

            User.builder()
                .username(authAdminUser.email.value)
                .password(authAdminUser.password.value)
                .authorities(authorities)
                .build()
        }
    }
}

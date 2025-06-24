package com.spotteacher.admin.shared.auth.infra

import com.spotteacher.admin.feature.adminUser.domain.AdminUserRepository
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
class FindAdminUserImpl(
    private val authUserRepository: AuthUserRepository,
    private val adminUserRepository: AdminUserRepository,
) : ReactiveUserDetailsService {

    override fun findByUsername(username: String?): Mono<UserDetails> {
        if (username == null) {
            return Mono.empty()
        }

        return mono {
            // このブロック内ではsuspend関数を安全に呼び出せる
            val authAdminUser =  authUserRepository.findByEmail(EmailAddress(username))?: throw UsernameNotFoundException(
                "User not found"
            )
            // UserDetailsオブジェクトを構築して返す
            // このブロックの最後の式の結果が、Monoが発行する値になる
            val authorities = listOf(SimpleGrantedAuthority("ADMIN"))
            User.builder()
                .username(authAdminUser.email.value)
                .password(authAdminUser.password.value)
                .authorities(authorities)
                .build()
        }
    }
}

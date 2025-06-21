package com.spotteacher.admin.shared.auth.refreshToken.infra

import com.spotteacher.admin.feature.adminUser.domain.AdminUserRepository
import com.spotteacher.domain.EmailAddress
import kotlinx.coroutines.reactor.mono
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Component
class FindAdminUserImpl (private val userRepository: AdminUserRepository) : ReactiveUserDetailsService {

    override fun findByUsername(username: String?): Mono<UserDetails> {
        if (username == null) {
            return Mono.empty()
        }

        return mono {
            // このブロック内ではsuspend関数を安全に呼び出せる
            val user = userRepository.findByEmailAndActiveUser(EmailAddress(username)) ?: throw UsernameNotFoundException(
                "User not found"
            )

            // UserDetailsオブジェクトを構築して返す
            // このブロックの最後の式の結果が、Monoが発行する値になる
            val authorities = listOf(SimpleGrantedAuthority("ROLE_ADMIN"))
            User.builder()
                .username(user.email.value)
                .password(user.password.value)
                .authorities(authorities)
                .build()
        }
    }
}

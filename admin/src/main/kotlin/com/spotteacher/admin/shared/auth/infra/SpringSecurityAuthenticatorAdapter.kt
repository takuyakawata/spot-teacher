package com.spotteacher.admin.shared.auth.infra

import com.spotteacher.admin.feature.adminUser.domain.ActiveAdminUser
import com.spotteacher.admin.feature.adminUser.domain.AdminUser
import com.spotteacher.admin.feature.adminUser.domain.AdminUserRepository
import com.spotteacher.admin.feature.adminUser.domain.Password
import com.spotteacher.admin.shared.auth.domain.Authenticator
import com.spotteacher.admin.shared.domain.Password
import com.spotteacher.domain.EmailAddress
import com.spotteacher.exception.ResourceNotFoundException
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component

@Component // インフラ層のSpringコンポーネント
class SpringSecurityAuthenticatorAdapter(
    private val authenticationManager: ReactiveAuthenticationManager,
    private val userRepository: AdminUserRepository
) : Authenticator {
    override suspend fun authenticate(email: EmailAddress, rawPassword: Password): ActiveAdminUser {
        val authenticationToken = UsernamePasswordAuthenticationToken(email.value, rawPassword.value)
        val authenticated = try {
            authenticationManager.authenticate(authenticationToken).awaitSingleOrNull()
            // 認証マネージャーがnullを返した場合（認証失敗）
                ?: throw AuthenticationFailedException("Authentication failed: No user found or not authenticated.")
        } catch (e: BadCredentialsException) {
            // パスワード間違いなど、BadCredentialsExceptionをキャッチ
            throw AuthenticationFailedException("Invalid credentials", e)
        } catch (e: UsernameNotFoundException) {
            // UserDetailsServiceImplから伝播したユーザー見つからずの例外をキャッチ
            throw AuthenticationFailedException("Invalid credentials", e)
        } catch (e: Exception) {
            // その他の認証時の予期せぬエラー
            throw AuthenticationFailedException("Authentication error occurred", e)
        }

        val adminUser = userRepository.findByEmailAndActiveUser(email)
            ?: throw ResourceNotFoundException(
                clazz = AdminUser::class,
                params = mapOf("email" to email.value)
            )

        return adminUser
    }
}

class AuthenticationFailedException(message: String, cause: Throwable? = null) : Exception(message, cause)

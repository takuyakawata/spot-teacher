package com.spotteacher.admin.config.auth

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class SecurityConfig {

//    // 認証エンドポイント用フィルターチェーン (優先度1)
//    @Bean
//    @Order(1)
//    fun authEndpointsFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
//        return http.securityMatcher(PathPatternParserServerWebExchangeMatcher("/api/admin/auth/**"))
//            .authorizeExchange { exchanges ->
//                // "/api/auth/**" へのリクエストはすべて許可
//                exchanges.anyExchange().permitAll()
//            }
//            .csrf(ServerHttpSecurity.CsrfSpec::disable)
//            .build()
//    }

    // API保護用フィルターチェーン (優先度2)
    @Bean
    @Order(1)
    fun apiEndpointsFilterChain(http: ServerHttpSecurity, jwtDecoder: ReactiveJwtDecoder): SecurityWebFilterChain {
        return http.authorizeExchange { exchanges ->
            exchanges
                .pathMatchers("/api/admin/auth/**").permitAll()
                .pathMatchers("/actuator/health").permitAll()
                .pathMatchers("/graphql").permitAll()
                .anyExchange().authenticated()
        }
            .oauth2ResourceServer { oauth2 ->
                oauth2.jwt { jwt ->
                    // カスタムのJWTデコーダーを設定
                    jwt.jwtDecoder(jwtDecoder) // API保護用フィルターチェーン (優先度2)
                    @Bean
                    @Order(2)
                    fun apiEndpointsFilterChain(http: ServerHttpSecurity, jwtDecoder: ReactiveJwtDecoder): SecurityWebFilterChain {
                        return http
                            // /api/auth/** 以外に適用
                            .authorizeExchange { exchanges ->
                                exchanges
                                    .pathMatchers("/actuator/health").permitAll()
                                    .anyExchange().authenticated()
                            }
                            .oauth2ResourceServer { oauth2 ->
                                oauth2.jwt { jwt -> jwt.jwtDecoder(jwtDecoder) }
                            }
                            .csrf(ServerHttpSecurity.CsrfSpec::disable)
                            .build()
                    }
                }
            }
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .build()
    }

    // ローカルの秘密鍵でJWTを検証するデコーダー
    @Bean
    fun jwtDecoder(jwtProvider: JwtProvider): ReactiveJwtDecoder {
        return NimbusReactiveJwtDecoder.withSecretKey(jwtProvider.secretKey).build()
    }

    // パスワードのハッシュ化に使うエンコーダー
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    // ★ この Bean を追加または修正します ★
    // ReactiveAuthenticationManagerを明示的にBeanとして提供
    @Bean
    fun reactiveAuthenticationManager(
        userDetailsService: ReactiveUserDetailsService, // UserDetailsServiceImplがDIされる
        passwordEncoder: PasswordEncoder // passwordEncoder()メソッドがDIされる
    ): ReactiveAuthenticationManager {
        return UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService).apply {
            setPasswordEncoder(passwordEncoder)
        }
    }
}

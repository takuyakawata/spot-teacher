package com.spotteacher.admin.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity.AuthorizeExchangeSpec
import org.springframework.security.config.web.server.ServerHttpSecurity.CsrfSpec
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import reactor.netty.resources.ConnectionProvider
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class SecurityConfigWithCognito(private val securityFilter: SecurityFilter) {

//    @Value("\${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
//    private lateinit var issuerUri: String
//
//    @Bean
//    @Throws(Exception::class)
//    fun springWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
//        return http.csrf { csrf: CsrfSpec -> csrf.disable() }.authorizeExchange { exchanges: AuthorizeExchangeSpec ->
//            exchanges
//                .pathMatchers("/graphql").permitAll()
//                .pathMatchers("/subscriptions").permitAll()
//                .pathMatchers("/actuator/health").permitAll()
//                .anyExchange().denyAll()
//        }.oauth2ResourceServer { oauth2ResourceServer ->
//            oauth2ResourceServer.jwt {}
//        }.addFilterBefore(securityFilter, SecurityWebFiltersOrder.AUTHENTICATION).build()
//    }
//
//    @Bean
//    fun nimbusReactiveJwtDecoder(): ReactiveJwtDecoder {
//        val webClient = WebClient.builder().clientConnector(
//            ReactorClientHttpConnector(
//                HttpClient.create(
//                    ConnectionProvider.builder(
//                        "custom-reactive-jwt-decoder-client"
//                    ).maxIdleTime(20.seconds.toJavaDuration())
//                        .maxLifeTime(60.seconds.toJavaDuration())
//                        .build()
//                )
//            )
//        ).build()
//
//        return NimbusReactiveJwtDecoder.withIssuerLocation(issuerUri)
//            .webClient(webClient)
//            .build()
//    }
}

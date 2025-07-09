package com.spotteacher.teacher.config.auth

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class SecurityConfigWithCognito() {

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

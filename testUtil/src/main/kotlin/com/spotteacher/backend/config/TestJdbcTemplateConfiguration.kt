package com.spotteacher.backend.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate

@Configuration
class TestJdbcTemplateConfiguration {

    @Bean
    fun jdbcTemplate(
        @Value("\${spring.datasource.url}") url: String,
        @Value("\${spring.datasource.username}") user: String,
        @Value("\${spring.datasource.password}") password: String,
        @Value("\${spring.datasource.hikari.maximum-pool-size}") maximumPoolSize: Int,
    ): JdbcTemplate = JdbcTemplate(
        HikariDataSource(
            HikariConfig().apply {
                this.jdbcUrl = url
                this.username = user
                this.password = password
                this.maximumPoolSize = maximumPoolSize
            }
        )
    )
}

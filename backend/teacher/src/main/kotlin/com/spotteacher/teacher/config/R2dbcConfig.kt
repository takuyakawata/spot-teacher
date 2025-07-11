package com.spotteacher.teacher.config

import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactory
import io.r2dbc.spi.ConnectionFactoryOptions
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
class R2dbcConfig {

    @Value("\${aurora.writer.url}")
    private lateinit var writerUrl: String

    @Value("\${aurora.writer.username}")
    private lateinit var writerUsername: String

    @Value("\${aurora.writer.password}")
    private lateinit var writerPassword: String

    @Value("\${aurora.reader.url}")
    private lateinit var readerUrl: String

    @Value("\${aurora.reader.username}")
    private lateinit var readerUsername: String

    @Value("\${aurora.reader.password}")
    private lateinit var readerPassword: String

    @Bean(name = ["writerConnectionFactory"])
    @Primary
    fun writerConnectionFactory(): ConnectionFactory {
        return createConnectionFactory(writerUrl, writerUsername, writerPassword)
    }

    @Bean(name = ["readerConnectionFactory"])
    fun readerConnectionFactory(): ConnectionFactory {
        return createConnectionFactory(readerUrl, readerUsername, readerPassword)
    }

    private fun createConnectionFactory(url: String, username: String, password: String): ConnectionFactory {
        val options = ConnectionFactoryOptions.parse(url).mutate()
            .option(ConnectionFactoryOptions.USER, username)
            .option(ConnectionFactoryOptions.PASSWORD, password)
            .build()
        return ConnectionFactories.get(options)
    }
}

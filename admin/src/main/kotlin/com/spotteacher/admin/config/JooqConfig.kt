package com.spotteacher.admin.config

import io.r2dbc.spi.ConnectionFactory
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.transaction.annotation.EnableTransactionManagement

/**
 * Configuration class for setting up JOOQ-related beans and managing database connections
 * for both write and read operations.
 *
 * ここでは主に、コマンドクエリの分離を行うための制約をつけています。
 *
 * This class enables transaction management and defines two `DSLContext` beans for writer and reader operations.
 * By utilizing the connection factories provided in the context, the class creates DSL contexts
 * configured for a MySQL SQLDialect.
 *
 * @property writerConnectionFactory The connection factory used for the writer DSL context.
 * @property readerConnectionFactory The connection factory used for the reader DSL context.
 */
@Configuration
@EnableTransactionManagement
class JooqConfig(
    @Qualifier("writerConnectionFactory") private val writerConnectionFactory: ConnectionFactory,
    @Qualifier("readerConnectionFactory") private val readerConnectionFactory: ConnectionFactory,
) {

    @Bean(name = ["writerDslContext"])
    @Primary
    fun writerDslContext(): DSLContext {
        return DSL.using(writerConnectionFactory, SQLDialect.MYSQL).dsl()
    }

    @Bean(name = ["readerDslContext"])
    fun readerDslContext(): DSLContext {
        return DSL.using(readerConnectionFactory, SQLDialect.MYSQL).dsl()
    }
}

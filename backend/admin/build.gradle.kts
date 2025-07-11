import com.expediagroup.graphql.plugin.gradle.graphql
import com.expediagroup.graphql.plugin.gradle.tasks.GraphQLGenerateSDLTask
import io.gitlab.arturbosch.detekt.getSupportedKotlinVersion
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.expediagroup.graphql)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

buildscript {
    dependencies {
        classpath("org.flywaydb:flyway-mysql:${libs.versions.flyway.get()}")
    }
}

group = "com.backend"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

// KotlinプロジェクトでJavaのアノテーションプロセッサ（Lombokなど）が
// 正しく動作するために、compileOnlyがannotationProcessorの依存を継承するよう設定
configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    //kotlin
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlin.coroutines.reactor)
    implementation(libs.reactor.kotlin.extensions)

    // spring
    implementation(libs.spring.boot.webflux)
    implementation(libs.spring.boot.aop)
    implementation(libs.spring.boot.security)

    // auth
    implementation(libs.spring.boot.oauth2.resource.server)
    implementation(libs.spring.security.oauth2.jose)
    implementation(libs.spring.security.oauth2.client)
    implementation(libs.jjwt.api)
    implementation(libs.jjwt.impl)
    implementation(libs.jjwt.jackson)

    // validation
    implementation(libs.valiktor.core)

    // r2dbcの設定
    implementation(libs.spring.boot.data.r2dbc)

    // health check
    implementation(libs.spring.boot.actuator)

    // graphql
    implementation(libs.graphql.kotlin.spring.server)
    implementation(libs.graphql.kotlin.schema.generator)
    implementation(libs.graphql.kotlin.hooks.provider)
    implementation(libs.graphql.java.extended.scalars)

    // flyway
    implementation(libs.flyway.core)
    runtimeOnly(libs.flyway.mysql)

    // jooq
    implementation(libs.spring.boot.jooq)
    implementation(libs.jooq.kotlin)
    implementation(libs.jooq.kotlin.coroutines)
    implementation(libs.jooq.reactor.extensions)
    implementation(libs.jooq.core)
    implementation(libs.jooq.meta)

    implementation(libs.jakarta.servlet.api)
    implementation(project(":db"))

    //  R2DBC
    implementation(libs.r2dbc.mysql)
    runtimeOnly(libs.r2dbc.pool)

    implementation(project(":shared"))

    // test
    testImplementation(libs.spring.boot.test)
    testImplementation(libs.spring.security.test)
//    testImplementation(libs.spring.security.oauth2.client)
    testImplementation(libs.awaitility)
    testRuntimeOnly(libs.kotlin.compiler.embeddable)
    testImplementation(libs.jackson.datatype.jsr310)
    testImplementation(project(":testUtil"))

    implementation(libs.aws.sdk.core)
    implementation(libs.aws.sdk.cloudfront)
    //depend
    implementation(libs.aws.sdk.s3)

    // kotest
    testImplementation(libs.kotest.runner.junit5)
    testImplementation(libs.kotest.framework.datatest)

    testImplementation(libs.kotest.extensions.spring)
    testImplementation(libs.kotest.extensions.mockserver)
    testImplementation(libs.springmockk)

    // mockk
    testImplementation(libs.mockk)
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
        jvmTarget.set(JvmTarget.JVM_21)
    }
}

graphql {
    schema {
        packages = listOf("com.spotteacher")
    }
}

val graphqlGenerateSDL by tasks.getting(GraphQLGenerateSDLTask::class) {
    packages.set(listOf("com.spotteacher.admin"))

    schemaFile.set(file("${project.projectDir}/graphql/schema.graphql"))
}

tasks.withType<Test> {
    useJUnitPlatform()
}

configurations.detekt {
    resolutionStrategy.eachDependency {
        if (requested.group == "org.jetbrains.kotlin") {
            useVersion(getSupportedKotlinVersion())
        }
    }
}

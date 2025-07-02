
import io.gitlab.arturbosch.detekt.getSupportedKotlinVersion
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}


dependencies {
    compileOnly(project(":shared"))
    compileOnly(project(":db"))

    // kotlin
    implementation(libs.kotlin.reflect)

    // spring boot
    compileOnly("org.springframework.boot:spring-boot-starter")
    compileOnly(libs.spring.boot.test)
    implementation(libs.spring.boot.aop)
    implementation(libs.spring.boot.data.r2dbc)
    implementation(libs.spring.boot.jooq)
    implementation(libs.spring.security.test)

    // cognito
    implementation(libs.spring.boot.oauth2.resource.server)
    implementation(libs.spring.security.oauth2.client)

    // graphql
    implementation(libs.graphql.kotlin.spring.server)

    // kotest
    compileOnly(libs.kotest.runner.junit5)
    compileOnly(libs.kotest.framework.datatest)
    compileOnly(libs.kotest.extensions.mockserver)
    compileOnly(libs.springmockk)

    compileOnly(libs.jackson.kotlin)
    compileOnly(libs.mysql.connector)
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

/**
 * This setting is necessary until Detekt, which can deal with Kotlin 2.1.0 is released.
 */
configurations.detekt {
    resolutionStrategy.eachDependency {
        if (requested.group == "org.jetbrains.kotlin") {
            useVersion(getSupportedKotlinVersion())
        }
    }
}


tasks.withType<BootJar> {
    enabled = false
}

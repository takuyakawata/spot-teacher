//import io.gitlab.arturbosch.detekt.getSupportedKotlinVersion
import io.gitlab.arturbosch.detekt.getSupportedKotlinVersion
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}



java.sourceCompatibility = JavaVersion.VERSION_21

repositories {
    mavenCentral()
}

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

    // spring boot
    implementation(libs.spring.boot.webflux)
    implementation(libs.spring.boot.aop)
    implementation(libs.spring.boot.data.r2dbc)

    // validation
    implementation(libs.valiktor.core)

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
    implementation(libs.jooq.core)
    implementation(libs.jooq.meta)

    implementation(libs.jakarta.servlet.api)
    //  R2DBC
    implementation(libs.r2dbc.mysql)

    implementation(libs.jackson.kotlin)

    // test
    testImplementation(libs.spring.boot.test)
    testImplementation(libs.spring.security.test)
    testImplementation(libs.awaitility)
    testRuntimeOnly(libs.kotlin.compiler.embeddable)
    testImplementation(libs.jackson.datatype.jsr310)

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

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
    enabled = false // bootJar タスクを無効化
}

configurations.detekt {
    resolutionStrategy.eachDependency {
        if (requested.group == "org.jetbrains.kotlin") {
            useVersion(getSupportedKotlinVersion())
        }
    }
}

import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.kotlin.dsl.withType
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.jooq)
    //todo detekt の追加
}

buildscript {
    dependencies {
        // flywayの設定migration tool
        classpath("org.flywaydb:flyway-mysql:11.8.2")
    }
}

//versionの統一処理
group = "com.example.spotteacher"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

extra["snippetsDir"] = file("build/generated-snippets")

// 配下のモジュールに共通の設定を与える
subprojects {
    apply(plugin = "kotlin")
    apply(plugin = "kotlin-spring")
    apply(plugin = "org.springframework.boot")
//    apply(plugin = "io.gitlab.arturbosch.detekt")
    repositories {
        mavenCentral()
    }

    java.sourceCompatibility = JavaVersion.VERSION_21

    dependencies {
        // kotlin
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

        // Util
        implementation("io.arrow-kt:arrow-core:1.2.4")
        implementation("io.arrow-kt:arrow-fx-coroutines:2.1.0")

        // todo 後で対応 detekt

    }

    tasks.withType<KotlinCompile> {
        compilerOptions {
            freeCompilerArgs.add("-Xjsr305=strict")
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

    tasks.withType<Copy> {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    tasks.test {
        useJUnitPlatform()
        testLogging {
            events("started", "passed", "skipped", "failed")
            exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
            showCauses = true
            showExceptions = true
            showStackTraces = true
        }
        jvmArgs = listOf(
            "--add-opens",
            "java.base/java.time=ALL-UNNAMED",
            "--add-opens",
            "java.base/java.util=ALL-UNNAMED",
        )
        systemProperty("spring.profiles.active", "test")
    }

    //todo detektの依存関係の追加を対応
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

dependencies {
    // health check
    implementation("org.springframework.boot:spring-boot-starter-actuator")
}

tasks.named<Jar>("jar") {
    enabled = false
}

tasks.withType<BootJar> {
    enabled = false
}

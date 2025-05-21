import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.dgs.codegen)
    alias(libs.plugins.asciidoctor)
    alias(libs.plugins.jooq)
    alias(libs.plugins.flyway)
}

//versionの統一処理
group = "com.example"
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

dependencies {
    // ===========================
    // 🔌 アプリ本体 - コア機能群
    // ===========================
    implementation(libs.spring.boot.starter.web)           // 通常のMVC・RESTエンドポイント
    implementation(libs.spring.boot.starter.graphql)       // GraphQL APIサポート
    implementation(libs.spring.boot.starter.security)      // 認証・認可（JWT, セッション等）
    implementation(libs.spring.boot.starter.validation)    // Bean Validation（@Valid など）
    implementation(libs.jackson.module.kotlin)             // Kotlinのdata classでのJSON変換対応
    implementation(libs.kotlin.reflect)                    // Kotlinリフレクション（DIで必要）

    // ===========================
    // 🔁 Spring Integration 系
    // ===========================
    implementation(libs.spring.boot.starter.integration)   // Integration本体（メッセージ連携）
    implementation(libs.spring.integration.http)           // HTTP inbound/outbound gateway
    implementation(libs.spring.integration.mail)           // メール連携（送信/受信）
    implementation(libs.spring.integration.jdbc)           // DB経由のメッセージ連携

    // ===========================
    // 📧 メール処理
    // ===========================
    implementation(libs.spring.boot.starter.mail)          // JavaMail によるメール送信

    // ===========================
    // 🧵 バッチ処理
    // ===========================
    implementation(libs.spring.boot.starter.batch)         // Spring Batch本体

    // ===========================
    // 🛡️ セキュリティ補助
    // ===========================
    implementation(libs.spring.security.messaging)         // WebSocket + Security など用

    // ===========================
    // 🐳 開発用（Docker Compose連携）
    // ===========================
    developmentOnly(libs.spring.boot.docker.compose)

    // ===========================
    // 🗃️ データベース / マイグレーション
    // ===========================
    implementation(libs.spring.boot.starter.data.jdbc)     // Spring JDBC Template
    implementation(libs.flyway.core)                       // Flyway 本体（マイグレーション）
    implementation(libs.flyway.mysql)                      // Flyway + MySQL対応
    runtimeOnly(libs.mysql.connector)
    // MySQL JDBCドライバ

    // ===========================
    // 🧪 テスト関連
    // ===========================
    testImplementation(libs.spring.boot.starter.test)      // 標準テスト（Junit, AssertJ 等）
    testImplementation(libs.kotlin.test.junit5)            // Kotlin用のJUnit5サポート
    testImplementation(libs.spring.batch.test)             // Spring Batch用テストユーティリティ
    testImplementation(libs.spring.graphql.test)           // GraphQL用のテストツール
    testImplementation(libs.spring.integration.test)       // Integration用のテストサポート
    testImplementation(libs.spring.restdocs.mockmvc)       // REST Docs生成用
    testImplementation(libs.spring.security.test)          // Securityコンテキストのテスト
    testRuntimeOnly(libs.junit.platform.launcher)          // IDE等でのJUnit起動補助


    // graphql
    implementation(libs.graphql.kotlin.spring.server)
    implementation(libs.graphql.kotlin.schema.generator)
    implementation(libs.graphql.kotlin.hooks.provider)
    implementation(libs.graphql.java.extended.scalars)

    // flyway
    implementation(libs.flyway.core)
    implementation(libs.flyway.mysql)

    // jooq
    implementation(libs.spring.boot.jooq)
    implementation(libs.jooq.kotlin)
    implementation(libs.jooq.kotlin.coroutines)
    implementation(libs.jooq.reactor.extensions)
    implementation(libs.jooq.core)
    implementation(libs.jooq.meta)
}


kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.generateJava {
    schemaPaths.add("${projectDir}/src/main/resources/graphql-client")
    packageName = "com.example.spotteacher.codegen"
    generateClient = true
}

tasks.withType<Test> {
    useJUnitPlatform()
}

//val graphqlGenerateSDL by tasks.getting(com.expediagroup.graphql.plugin.gradle.tasks.GraphQLGenerateSDLTask::class) {
//    packages.set(listOf("com.cosoji.todo"))
//
//    schemaFile.set(file("${project.projectDir}/graphql/schema.graphql"))
//}

tasks.test {
    outputs.dir(project.extra["snippetsDir"]!!)
}

tasks.asciidoctor {
    inputs.dir(project.extra["snippetsDir"]!!)
    dependsOn(tasks.test)
}

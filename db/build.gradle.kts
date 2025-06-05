import org.jooq.meta.jaxb.ForcedType

plugins {
    alias(libs.plugins.jooq)
    alias(libs.plugins.flyway)
}

repositories {
    mavenCentral()
}

buildscript {
    dependencies {
        classpath("org.flywaydb:flyway-mysql:${libs.versions.flyway.get()}")
    }
}

dependencies {
    // amazon jdbc
    runtimeOnly(libs.mysql.connector)
//    implementation(libs.aws.jdbc.wrapper)

    // jooq
    implementation(libs.jooq.core)
    implementation(libs.jooq.kotlin)
    implementation(libs.jooq.kotlin.coroutines)
    implementation(libs.jooq.codegen)
    implementation(libs.jooq.meta)
    jooqCodegen(libs.mysql.connector)
}

object DB {
    const val USER = "user"
    const val PASSWORD = "password"
    const val URL =
        "jdbc:mysql://127.0.0.1:3306/spot_teacher?autoReconnect=true&useSSL=false&allowPublicKeyRetrieval=true"
    const val DB_NAME = "spot_teacher"
}

flyway {
    url = DB.URL
    user = DB.USER
    password = DB.PASSWORD
}

jooq {
    configuration {
        jdbc {
            user = DB.USER
            password = DB.PASSWORD
            url = DB.URL
            driver = "com.mysql.cj.jdbc.Driver"
        }
        generator {
            name = "org.jooq.codegen.KotlinGenerator"
            database {
                name = "org.jooq.meta.mysql.MySQLDatabase"
                inputSchema = DB.DB_NAME
                isOutputSchemaToDefault = true
                excludes = "flyway_schema_history"
                forcedTypes.addAll(
                    listOf(
                        ForcedType().apply {
                            name = "BOOLEAN"
                            includeTypes = "(?i:TINYINT\\(1\\))"
                        }
                    )
                )
            }
            generate {
                isDeprecated = false
                isKotlinSetterJvmNameAnnotationsOnIsPrefix = true
                isKeys = false
                isIndexes = false
                isDefaultSchema = false
                isDefaultCatalog = false
                isKotlinNotNullRecordAttributes = true
            }
            target {
                packageName = "com.spotteacher.infra.db"
                directory = "${projectDir}/src/main/kotlin"
            }
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
    enabled = false // bootJar タスクを無効化
}

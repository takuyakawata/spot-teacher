package com.spotteacher.teacher

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.extensions.Extension
import io.kotest.extensions.spring.SpringAutowireConstructorExtension
import io.kotest.extensions.spring.SpringExtension
import org.flywaydb.core.Flyway
import org.flywaydb.core.internal.exception.FlywayMigrateException

class ProjectConfig : AbstractProjectConfig() {

    private val flyway = Flyway.configure().dataSource(
        System.getenv("SPOT_TEACHER_TEST_DB_URL")
            ?: "jdbc:mysql://localhost:3306/spot_teacher_test?useSSL=false&allowPublicKeyRetrieval=true",
        System.getenv("SPOT_TEACHER_TEST_DB_USER") ?: "user",
        System.getenv("SPOT_TEACHER_TEST_DB_PASSWORD") ?: "password"
    ).cleanDisabled(false).load()

    override fun extensions(): List<Extension> = listOf(SpringExtension, SpringAutowireConstructorExtension)

//    override val isolationMode: IsolationMode = IsolationMode.InstancePerLeaf

    override suspend fun beforeProject() {
        super.beforeProject()

        try {
            flyway.migrate()
        } catch (_: FlywayMigrateException) {
            flyway.clean()
            flyway.migrate()
        }
    }
}

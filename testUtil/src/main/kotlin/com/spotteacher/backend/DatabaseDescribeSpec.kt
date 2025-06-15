package com.spotteacher.backend

import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.DescribeSpec
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
open class DatabaseDescribeSpec(
    body: DescribeSpec.() -> Unit = {},
) : DescribeSpec(body) {
    private lateinit var internalJdbcTemplate: JdbcTemplate

    override suspend fun beforeSpec(spec: Spec) {
        super.beforeSpec(spec)

        val deleteTargetTableNames = internalJdbcTemplate.queryForList("show tables")
            .map {
                it.values.first() as String
            }
            .filterNot { tableName -> tableName == "flyway_schema_history" }

        internalJdbcTemplate.batchUpdate(
            "SET FOREIGN_KEY_CHECKS = 0",
            *deleteTargetTableNames.map {
                println("テーブル削除: $it")
                "DELETE FROM $it"
            }.toTypedArray(),
            "SET FOREIGN_KEY_CHECKS = 1",
        )

        println("データ削除成功！")
    }
}

package com.spotteacher.backend

import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.DescribeSpec
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
open class DatabaseDescribeSpec(
    body: DescribeSpec.() -> Unit = {},
) : DescribeSpec(body) {

//    private lateinit var internalJdbcTemplate: JdbcTemplate
    @Autowired
    protected open lateinit var databaseClient: DatabaseClient

    @Value("\${aurora.writer.url}")
    private lateinit var r2dbcUrl: String

    private val testDatabaseName: String by lazy { // lazyで遅延初期化
        // r2dbc URL (例: r2dbc:mysql://localhost:3306/spot_teacher_test) からデータベース名を抽出
        // "r2dbc:mysql://" 以降のパス部分を取得し、先頭の"/"を削除
        r2dbcUrl.substringAfterLast("/").substringBefore("?")
    }

    override suspend fun beforeSpec(spec: Spec) {
        super.beforeSpec(spec)

        databaseClient.sql("SET FOREIGN_KEY_CHECKS = 0").fetch().rowsUpdated().awaitSingle()

        val allTestTableNames = databaseClient.sql(
            """
            SELECT TABLE_NAME 
            FROM INFORMATION_SCHEMA.TABLES 
            WHERE TABLE_SCHEMA = :databaseName 
              AND TABLE_TYPE = 'BASE TABLE'
            """.trimIndent()
        ) // MySQLのINFORMATION_SCHEMAからテーブル名を取得
            .bind("databaseName", testDatabaseName) // 取得したテストDB名をクエリにバインド
            .map { row -> row.get("TABLE_NAME", String::class.java)!! } // "TABLE_NAME"カラムをStringとして取得
            .all() // すべての結果をFlux<String>として取得
            .collectList() // Flux<String>をMono<List<String>>に変換
            .awaitSingle() // Mono<List<String>>をsuspendコンテキストでList<String>に変換

        databaseClient.sql("SET FOREIGN_KEY_CHECKS = 1").fetch().rowsUpdated().awaitSingle()
        println("取得したテーブル名: $allTestTableNames")

        println("データ削除中...")
        allTestTableNames.forEach { tableName ->
            println("テーブルデータ削除: $tableName")
            databaseClient.sql("DELETE FROM $tableName").fetch().rowsUpdated().awaitSingle()
        }

        // Foreign Key Checksを再度有効化（非同期で実行）
        databaseClient.sql("SET FOREIGN_KEY_CHECKS = 1").fetch().rowsUpdated().awaitSingle()

        println("データ削除成功！")
    }
}

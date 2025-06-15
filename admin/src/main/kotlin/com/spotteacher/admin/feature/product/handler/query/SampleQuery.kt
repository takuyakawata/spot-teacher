package com.spotteacher.admin.feature.product.handler.query

import com.expediagroup.graphql.server.operations.Query
import org.springframework.stereotype.Component

/**
 * このクラスがGraphQLの 'Query' 型を定義します。
 * @Component を付けることで、Springとgraphql-kotlinがこのクラスを認識します。
 */
@Component
class SampleQuery : Query {

    /**
     * この関数が "hello" という名前のGraphQLクエリになります。
     * 例: hello(name: "Taro") -> "こんにちは、Taroさん！"
     */
    fun hello(name: String?): String {
        val target = name ?: "World"
        return "こんにちは、${target}さん！"
    }

    /**
     * この関数が "ping" という名前のGraphQLクエリになります。
     * 常に "pong" を返します。
     */
    fun ping(): String {
        return "pong"
    }
}

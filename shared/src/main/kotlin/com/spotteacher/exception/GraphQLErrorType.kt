package com.spotteacher.exception

import graphql.ErrorClassification

/**
 * エラーレスポンス用 GraphQL の例外 enum
 */
enum class GraphQLErrorType : ErrorClassification {
    ResourceNotFoundException,
    UnknownException,
}

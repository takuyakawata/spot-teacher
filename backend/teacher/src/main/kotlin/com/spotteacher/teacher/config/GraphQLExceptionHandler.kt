package com.spotteacher.teacher.config

import com.spotteacher.exception.GraphQLErrorType
import com.spotteacher.exception.ResourceNotFoundException
import com.spotteacher.util.logError
import graphql.GraphQLError
import graphql.GraphqlErrorBuilder
import graphql.execution.DataFetcherExceptionHandler
import graphql.execution.DataFetcherExceptionHandlerParameters
import graphql.execution.DataFetcherExceptionHandlerResult
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture

/**
 * `DataFetcherExceptionHandler` のカスタム実装
 * handler 以降の処理で発生したエラーはここで処理する
 */
@Component
class GraphQLExceptionHandler : DataFetcherExceptionHandler {
    override fun handleException(params: DataFetcherExceptionHandlerParameters): CompletableFuture<DataFetcherExceptionHandlerResult> {
        val exception = params.exception
        logError({ "GraphQL Exception: ${exception.message}" }, exception)

        val error: GraphQLError = when (exception) {
            is ResourceNotFoundException -> {
                GraphqlErrorBuilder.newError()
                    .message(exception.message)
                    .path(params.path.toList())
                    .locations(listOf(params.sourceLocation))
                    .errorType(GraphQLErrorType.ResourceNotFoundException)
                    .extensions(mapOf("classification" to GraphQLErrorType.ResourceNotFoundException))
                    .build()
            }

            else -> {
                GraphQLError.newError()
                    .message(exception.message ?: "Unknown error")
                    .path(params.path.toList())
                    .locations(listOf(params.sourceLocation))
                    .errorType(GraphQLErrorType.UnknownException)
                    .extensions(mapOf("classification" to GraphQLErrorType.UnknownException))
                    .build()
            }
        }

        return CompletableFuture.completedFuture(
            DataFetcherExceptionHandlerResult.newResult().error(error).build()
        )
    }
}

package com.spotteacher.admin.feature.product.handler.query

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.expediagroup.graphql.generator.scalars.ID
import com.expediagroup.graphql.server.operations.Query
import com.spotteacher.admin.feature.product.domain.ProductError
import com.spotteacher.admin.feature.product.domain.ProductId
import com.spotteacher.admin.feature.product.handler.mutation.ProductType
import com.spotteacher.admin.feature.product.handler.mutation.toGraphQLID
import com.spotteacher.admin.feature.product.usecase.FindProductUseCase
import com.spotteacher.admin.feature.product.usecase.FindProductUseCaseInput
import com.spotteacher.graphql.toDomainId
import org.springframework.stereotype.Component

data class FindProductQueryOutput(
    val result: Either<ProductError,ProductType>
)

@Component
class ProductQuery(
    private val findProductUseCase: FindProductUseCase
) : Query {

    suspend fun product(productId: ID) : FindProductQueryOutput {
        val result = findProductUseCase.call(
            FindProductUseCaseInput(productId.toDomainId(::ProductId))
        ).result

        // ラムダ式を使った改善案
        return result.fold(
            ifLeft = { error -> FindProductQueryOutput(error.left()) },
            ifRight = { product ->
                FindProductQueryOutput(
                    ProductType(
                        id = product.id.toGraphQLID(),
                        name = product.name.value,
                        price = product.price.value,
                        description = product.description?.value,
                    ).right()
                )
            }
        )
    }
}

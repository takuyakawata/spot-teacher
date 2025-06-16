package com.spotteacher.admin.feature.product.handler.query

import com.expediagroup.graphql.generator.scalars.ID
import com.expediagroup.graphql.server.operations.Query
import com.spotteacher.admin.feature.product.domain.ProductError
import com.spotteacher.admin.feature.product.domain.ProductId
import com.spotteacher.admin.feature.product.handler.mutation.ProductType
import com.spotteacher.admin.feature.product.handler.mutation.toGraphQLID
import com.spotteacher.admin.feature.product.usecase.FindProductUseCase
import com.spotteacher.admin.feature.product.usecase.FindProductUseCaseInput
import com.spotteacher.admin.feature.product.usecase.FindProductsUseCase
import com.spotteacher.admin.feature.product.usecase.FindProductsUseCaseInput
import com.spotteacher.domain.SortOrder
import com.spotteacher.graphql.toDomainId
import org.springframework.stereotype.Component

sealed interface FindProductQueryOutput{
    data class FindProductQuerySuccess(val product: ProductType) : FindProductQueryOutput
    data class FindProductQueryError(val error: ProductError) : FindProductQueryOutput
}

sealed interface FindProductsQueryOutput{
    data class FindProductsQuerySuccess(val products: List<ProductType>) : FindProductsQueryOutput
    data class FindProductsQueryError(val error: ProductError) : FindProductsQueryOutput
}

@Component
class ProductQuery(
    private val findProductUseCase: FindProductUseCase,
    private val findProductsUseCase: FindProductsUseCase
) : Query {

    suspend fun product(productId: ID) : FindProductQueryOutput {
        val result = findProductUseCase.call(
            FindProductUseCaseInput(productId.toDomainId(::ProductId))
        ).result

        return result.fold(
            ifLeft = { error -> FindProductQueryOutput.FindProductQueryError(error) },
            ifRight = { product ->
                FindProductQueryOutput.FindProductQuerySuccess(
                    ProductType(
                        id = product.id.toGraphQLID(),
                        name = product.name.value,
                        price = product.price.value,
                        description = product.description?.value,
                    )
                )
            }
        )
    }

    suspend fun products(
        limit: Int = 10,
        lastId: String? = null,
        sortOrder: String = "DESC"
    ) : FindProductsQueryOutput {
        val order = when (sortOrder.uppercase()) {
            "ASC" -> SortOrder.ASC
            else -> SortOrder.DESC
        }

        val result = findProductsUseCase.call(
            FindProductsUseCaseInput(
                limit = limit,
                lastId = lastId?.toLongOrNull(),
                sortOrder = order
            )
        ).result

        return result.fold(
            ifLeft = { error -> FindProductsQueryOutput.FindProductsQueryError(error)},
            ifRight = { products ->
                FindProductsQueryOutput.FindProductsQuerySuccess(
                    products.map { product ->
                        ProductType(
                            id = product.id.toGraphQLID(),
                            name = product.name.value,
                            price = product.price.value,
                            description = product.description?.value,
                        )
                    }
                )
            }
        )
    }
}

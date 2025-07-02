package com.spotteacher.admin.feature.product.usecase

import arrow.core.Either
import arrow.core.right
import com.spotteacher.admin.feature.product.domain.Product
import com.spotteacher.admin.feature.product.domain.ProductError
import com.spotteacher.admin.feature.product.domain.ProductId
import com.spotteacher.admin.feature.product.domain.ProductName
import com.spotteacher.admin.feature.product.domain.ProductPrice
import com.spotteacher.admin.feature.product.domain.ProductRepository
import com.spotteacher.admin.shared.infra.TransactionCoroutine
import com.spotteacher.domain.ColumnValue
import com.spotteacher.domain.Pagination
import com.spotteacher.domain.SortOrder
import com.spotteacher.usecase.UseCase

data class FindProductsUseCaseInput(
    val limit: Int,
    val lastId: Long? = null,
    val sortOrder: SortOrder = SortOrder.DESC
)

data class FindProductsUseCaseOutput(
    val result: Either<ProductError, List<Product>>
)

@UseCase
class FindProductsUseCase(
    private val productRepository: ProductRepository
) {
    @TransactionCoroutine
    suspend fun call(input: FindProductsUseCaseInput): FindProductsUseCaseOutput {
        val pagination = if (input.lastId != null) {
            // If lastId is provided, use cursor-based pagination
            val lastProduct = Product(
                id = ProductId(input.lastId),
                name = ProductName(""),
                price = ProductPrice(1),
                description = null
            )
            val columnValue = ColumnValue<Product, ProductId, Long>(
                column = Product::id,
                lastValue = lastProduct.id,
                order = input.sortOrder,
                transform = { id -> id.value }
            )
            Pagination<Product>(input.limit, columnValue)
        } else {
            // If lastId is not provided, just use limit
            Pagination<Product>(input.limit)
        }

        val products = productRepository.getPaginated(pagination)
        return FindProductsUseCaseOutput(products.right())
    }
}

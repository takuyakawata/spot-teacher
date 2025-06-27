package com.spotteacher.admin.feature.product.usecase

import com.spotteacher.admin.feature.product.domain.Product
import com.spotteacher.admin.feature.product.domain.ProductId
import com.spotteacher.admin.feature.product.domain.ProductRepository
import com.spotteacher.domain.ColumnValue
import com.spotteacher.domain.Pagination
import com.spotteacher.domain.SortOrder
import com.spotteacher.usecase.UseCase

data class FindPaginatedProductsUseCaseInput(
    val lastId: Pair<ProductId?, SortOrder>,
    val limit: Int
)

@UseCase
class FindPaginatedProductsUseCase(
    private val productRepository: ProductRepository
){
    suspend fun call(input: FindPaginatedProductsUseCaseInput):List<Product>{
        return productRepository.getPaginated(
            pagination = Pagination(
                limit = input.limit,
                cursorColumns = listOfNotNull(
                    ColumnValue(
                        column = Product::id,
                        lastValue = input.lastId.first?.value,
                        order = input.lastId.second,
                    ){ it }
                ).toTypedArray()
            )
        )
    }
}

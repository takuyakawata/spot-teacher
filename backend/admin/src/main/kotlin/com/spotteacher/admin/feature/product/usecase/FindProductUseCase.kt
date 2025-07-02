package com.spotteacher.admin.feature.product.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.spotteacher.admin.feature.product.domain.Product
import com.spotteacher.admin.feature.product.domain.ProductError
import com.spotteacher.admin.feature.product.domain.ProductErrorCode
import com.spotteacher.admin.feature.product.domain.ProductId
import com.spotteacher.admin.feature.product.domain.ProductRepository
import com.spotteacher.admin.shared.infra.TransactionCoroutine
import com.spotteacher.usecase.UseCase

data class FindProductUseCaseInput(
    val productId: ProductId
)

data class FindProductUseCaseOutput(
    val result: Either<ProductError, Product>
)

@UseCase
class FindProductUseCase(
    private val productRepository: ProductRepository
) {
    @TransactionCoroutine
    suspend fun call(input: FindProductUseCaseInput): FindProductUseCaseOutput {
        val product = productRepository.findById(input.productId) ?: return FindProductUseCaseOutput(
            ProductError(
                ProductErrorCode.PRODUCT_NOT_FOUND,
                "Product not found"
            ).left()
        )

        return FindProductUseCaseOutput(product.right())
    }
}

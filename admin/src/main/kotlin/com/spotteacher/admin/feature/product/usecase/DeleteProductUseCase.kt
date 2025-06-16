package com.spotteacher.admin.feature.product.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.spotteacher.admin.feature.product.domain.ProductError
import com.spotteacher.admin.feature.product.domain.ProductErrorCode
import com.spotteacher.admin.feature.product.domain.ProductId
import com.spotteacher.admin.feature.product.domain.ProductRepository
import com.spotteacher.admin.shared.infra.TransactionCoroutine
import com.spotteacher.usecase.UseCase

data class DeleteProductUseCaseInput(
    val id: ProductId
)

data class DeleteProductUseCaseOutput(
    val result: Either<ProductError, Unit>
)

@UseCase
class DeleteProductUseCase(
    private val productRepository: ProductRepository
) {
    @TransactionCoroutine
    suspend fun call(input: DeleteProductUseCaseInput): DeleteProductUseCaseOutput {
        // Check if product exists
        val existingProduct = productRepository.findById(input.id) ?: return DeleteProductUseCaseOutput(
            ProductError(
                ProductErrorCode.PRODUCT_NOT_FOUND,
                "Product not found"
            ).left()
        )

        // Delete product from repository
        productRepository.delete(input.id)

        // Return success
        return DeleteProductUseCaseOutput(Unit.right())
    }
}

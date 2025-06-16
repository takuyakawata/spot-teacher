package com.spotteacher.admin.feature.product.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.spotteacher.admin.feature.product.domain.Product
import com.spotteacher.admin.feature.product.domain.ProductDescription
import com.spotteacher.admin.feature.product.domain.ProductError
import com.spotteacher.admin.feature.product.domain.ProductErrorCode
import com.spotteacher.admin.feature.product.domain.ProductId
import com.spotteacher.admin.feature.product.domain.ProductName
import com.spotteacher.admin.feature.product.domain.ProductPrice
import com.spotteacher.admin.feature.product.domain.ProductRepository
import com.spotteacher.admin.shared.infra.TransactionCoroutine
import com.spotteacher.usecase.UseCase

data class UpdateProductUseCaseInput(
    val id: ProductId,
    val name: ProductName,
    val price: ProductPrice,
    val description: ProductDescription?
)

data class UpdateProductUseCaseOutput(
    val result: Either<ProductError, Product>
)

@UseCase
class UpdateProductUseCase(
    private val productRepository: ProductRepository
) {
    @TransactionCoroutine
    suspend fun call(input: UpdateProductUseCaseInput): UpdateProductUseCaseOutput {
        // Check if product exists
        val existingProduct = productRepository.findById(input.id) ?: return UpdateProductUseCaseOutput(
            ProductError(
                ProductErrorCode.PRODUCT_NOT_FOUND,
                "Product not found"
            ).left()
        )

        // Create updated product
        val updatedProduct = existingProduct.copy(
            name = input.name,
            price = input.price,
            description = input.description
        )

        // Update product in repository
        productRepository.update(updatedProduct)

        // Return success
        return UpdateProductUseCaseOutput(updatedProduct.right())
    }
}

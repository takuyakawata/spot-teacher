package com.spotteacher.admin.feature.product.usecase

import com.spotteacher.admin.feature.product.domain.Product
import com.spotteacher.admin.feature.product.domain.ProductDescription
import com.spotteacher.admin.feature.product.domain.ProductName
import com.spotteacher.admin.feature.product.domain.ProductPrice
import com.spotteacher.admin.feature.product.domain.ProductRepository
import com.spotteacher.usecase.UseCase

data class CreateProductUseCaseInput(
    val name: ProductName,
    val price: ProductPrice,
    val description: ProductDescription?
)

sealed interface CreateProductUseCaseOutput {
    data class Success(val success:Unit) : CreateProductUseCaseOutput
//    data class Error(val error: ProductErrorCode) : CreateProductUseCaseOutput
}

@UseCase
class CreateProductUseCase(
    private val productRepository: ProductRepository
) {
    suspend fun call(input: CreateProductUseCaseInput) : CreateProductUseCaseOutput{
        val newProduct = Product.create(
            name = input.name,
            price = input.price,
            description = input.description
        )

        productRepository.create(newProduct)
        return CreateProductUseCaseOutput.Success(Unit)
    }
}

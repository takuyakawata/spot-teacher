package com.spotteacher.admin.feature.product.handler.mutation

import com.expediagroup.graphql.server.operations.Mutation
import com.spotteacher.admin.feature.product.domain.ProductDescription
import com.spotteacher.admin.feature.product.domain.ProductName
import com.spotteacher.admin.feature.product.domain.ProductPrice
import com.spotteacher.admin.feature.product.usecase.CreateProductUseCase
import com.spotteacher.admin.feature.product.usecase.CreateProductUseCaseInput
import org.springframework.stereotype.Component

data class CreateProductMutationInput(
    val name: String,
    val price: Int,
    val description: String?
)

@Component
class CreateProductMutation(
    private val usecase: CreateProductUseCase
) : Mutation {
    suspend fun createProduct(input: CreateProductMutationInput) {
        usecase.call(
            CreateProductUseCaseInput(
                name = ProductName(input.name),
                price = ProductPrice(input.price),
                description = input.description?.let { ProductDescription(it) }
            )
        )
    }
}

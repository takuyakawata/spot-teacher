package com.spotteacher.admin.feature.product.handler.mutation

import com.expediagroup.graphql.generator.scalars.ID
import com.expediagroup.graphql.server.operations.Mutation
import com.spotteacher.admin.feature.product.domain.ProductDescription
import com.spotteacher.admin.feature.product.domain.ProductError
import com.spotteacher.admin.feature.product.domain.ProductId
import com.spotteacher.admin.feature.product.domain.ProductName
import com.spotteacher.admin.feature.product.domain.ProductPrice
import com.spotteacher.admin.feature.product.handler.ProductType
import com.spotteacher.admin.feature.product.handler.toGraphQLID
import com.spotteacher.admin.feature.product.usecase.UpdateProductUseCase
import com.spotteacher.admin.feature.product.usecase.UpdateProductUseCaseInput
import com.spotteacher.graphql.toDomainId
import org.springframework.stereotype.Component

data class UpdateProductMutationInput(
    val id: ID,
    val name: String?,
    val price: Int?,
    val description: String?
)

sealed interface UpdateProductMutationOutput {
    data class UpdateProductMutationSuccess(val product: ProductType) : UpdateProductMutationOutput
    data class UpdateProductMutationError(val error: ProductError) : UpdateProductMutationOutput
}

@Component
class UpdateProductMutation(
    private val usecase: UpdateProductUseCase
) : Mutation {
    suspend fun updateProduct(input: UpdateProductMutationInput): UpdateProductMutationOutput {
        val result = usecase.call(
            UpdateProductUseCaseInput(
                id = input.id.toDomainId(::ProductId),
                name = input.name?.let{ProductName(it)},
                price = input.price?.let{ProductPrice(it)},
                description = input.description?.let { ProductDescription(it) }
            )
        ).result

        return result.fold(
            ifLeft = { error -> UpdateProductMutationOutput.UpdateProductMutationError(error) },
            ifRight = { product ->
                UpdateProductMutationOutput.UpdateProductMutationSuccess(
                    ProductType(
                        id = product.id.toGraphQLID(),
                        name = product.name.value,
                        price = product.price.value,
                        description = product.description?.value
                    )
                )
            }
        )
    }
}

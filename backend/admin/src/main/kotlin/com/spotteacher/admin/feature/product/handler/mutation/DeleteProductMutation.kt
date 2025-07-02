package com.spotteacher.admin.feature.product.handler.mutation

import com.expediagroup.graphql.generator.scalars.ID
import com.expediagroup.graphql.server.operations.Mutation
import com.spotteacher.admin.feature.product.domain.ProductError
import com.spotteacher.admin.feature.product.domain.ProductId
import com.spotteacher.admin.feature.product.usecase.DeleteProductUseCase
import com.spotteacher.admin.feature.product.usecase.DeleteProductUseCaseInput
import com.spotteacher.graphql.toDomainId
import org.springframework.stereotype.Component

data class DeleteProductMutationInput(
    val id: ID
)

sealed interface DeleteProductMutationOutput {
    data class DeleteProductMutationSuccess(val success: Boolean) : DeleteProductMutationOutput
    data class DeleteProductMutationError(val error: ProductError) : DeleteProductMutationOutput
}

@Component
class DeleteProductMutation(
    private val usecase: DeleteProductUseCase
) : Mutation {
    suspend fun deleteProduct(input: DeleteProductMutationInput): DeleteProductMutationOutput {
        val result = usecase.call(
            DeleteProductUseCaseInput(
                id = input.id.toDomainId(::ProductId)
            )
        ).result

        return result.fold(
            ifLeft = { error -> DeleteProductMutationOutput.DeleteProductMutationError(error) },
            ifRight = { DeleteProductMutationOutput.DeleteProductMutationSuccess(true) }
        )
    }
}

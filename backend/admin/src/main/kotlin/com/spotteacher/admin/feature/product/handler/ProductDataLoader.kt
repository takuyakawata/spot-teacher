package com.spotteacher.admin.feature.product.handler

import arrow.core.Nel
import com.spotteacher.admin.feature.product.domain.ProductId
import com.spotteacher.admin.feature.product.domain.ProductRepository
import com.spotteacher.admin.shared.graphql.KotlinCoroutineDataLoader
import graphql.schema.DataFetchingEnvironment
import org.dataloader.DataLoader
import org.springframework.stereotype.Component

private const val DATALOADER_NAME = "ProductDataLoader"

@Component
class ProductDataLoader(private val repository: ProductRepository) : KotlinCoroutineDataLoader<ProductId, ProductType> {
    override val dataLoaderName = DATALOADER_NAME

    override suspend fun batchLoad(keys: Nel<ProductId>): List<ProductType> {
        val products = repository.filterByIds(keys)

        return products.map { product ->
            ProductType(
                id = product.id.toGraphQLID(),
                name = product.name.value,
                price = product.price.value,
                description = product.description?.value,
            )
        }
    }
}

fun DataFetchingEnvironment.getUserDataLoader(): DataLoader<ProductId, ProductType?> {
    return this.getDataLoader<ProductId, ProductType?>(DATALOADER_NAME)!!
}

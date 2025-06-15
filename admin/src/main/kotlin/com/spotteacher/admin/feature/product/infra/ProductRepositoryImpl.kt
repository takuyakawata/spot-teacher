package com.spotteacher.admin.feature.product.infra

import com.spotteacher.admin.feature.product.domain.Product
import com.spotteacher.admin.feature.product.domain.ProductId
import com.spotteacher.admin.feature.product.domain.ProductRepository
import org.springframework.stereotype.Component

@Component
class ProductRepositoryImpl : ProductRepository {
    override suspend fun findById(productId: ProductId): Product? {
        TODO("Not yet implemented")
    }

    override suspend fun getAll(): List<Product> {
        TODO("Not yet implemented")
    }

    override suspend fun create(product: Product) {
        TODO("Not yet implemented")
    }

    override suspend fun update(product: Product) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(productId: String) {
        TODO("Not yet implemented")
    }
}

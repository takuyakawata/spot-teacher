package com.spotteacher.admin.feature.product.domain

import arrow.core.Nel
import com.spotteacher.domain.Pagination

interface ProductRepository {
    suspend fun findById(id: ProductId): Product?
    suspend fun getAll(): List<Product>
    suspend fun getPaginated(pagination: Pagination<Product>): List<Product>
    suspend fun create(product: Product): Product
    suspend fun update(product: Product)
    suspend fun delete(productId: ProductId)
    suspend fun filterByIds(productIds: Nel<ProductId>): List<Product>
}

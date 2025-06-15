package com.spotteacher.admin.feature.product.domain

import org.springframework.stereotype.Component

interface ProductRepository{
    suspend fun findById(productId: ProductId): Product?
    suspend fun getAll(): List<Product>
    suspend fun create(product: Product)
    suspend fun update(product: Product)
    suspend fun delete(productId: String)
}

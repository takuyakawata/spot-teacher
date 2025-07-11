package com.spotteacher.admin.fixture

import com.spotteacher.admin.feature.product.domain.Product
import com.spotteacher.admin.feature.product.domain.ProductDescription
import com.spotteacher.admin.feature.product.domain.ProductId
import com.spotteacher.admin.feature.product.domain.ProductName
import com.spotteacher.admin.feature.product.domain.ProductPrice
import com.spotteacher.admin.feature.product.domain.ProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ProductFixture {

    @Autowired
    private lateinit var productRepository: ProductRepository

    private var productIdCount = 1L

    fun buildProduct(
        id: ProductId = ProductId(productIdCount++),
        name: String = "test product",
        price: Int = 1000,
        description: String? = "test description"
    ): Product {
        return Product(
            id = id,
            name = ProductName(name),
            price = ProductPrice(price),
            description = description?.let { ProductDescription(it) }
        )
    }

    suspend fun createProduct(
        name: String = "test product",
        price: Int = 1000,
        description: String? = "test description"
    ): Product {
        return productRepository.create(
            buildProduct(
                name = name,
                price = price,
                description = description
            )
        )
    }
}

package com.spotteacher.admin.feature.product.usecase

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
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk

class FindProductUseCaseTest : DescribeSpec({
    describe("FindProductUseCase") {
        // Arrange
        val productRepository = mockk<ProductRepository>()
        val usecase = FindProductUseCase(productRepository)
        val productId = ProductId(1)
        val product = Product(
            id = productId,
            name = ProductName("test product"),
            price = ProductPrice(1000),
            description = ProductDescription("test description")
        )

        describe("call") {
            context("when product exists") {
                it("should return Success with the product") {
                    // Arrange
                    coEvery { productRepository.findById(productId) } returns product

                    // Act
                    val result = usecase.call(
                        FindProductUseCaseInput(
                            productId = productId
                        )
                    )

                    // Assert
                    result.result shouldBe product.right()
                }
            }

            context("when product does not exist") {
                it("should return NotFound") {
                    // Arrange
                    coEvery { productRepository.findById(productId) } returns null

                    // Act
                    val result = usecase.call(
                        FindProductUseCaseInput(
                            productId = productId
                        )
                    )

                    // Assert
                    result.result shouldBe ProductError(
                        ProductErrorCode.PRODUCT_NOT_FOUND,
                        "Product not found"
                    ).left()
                }
            }
        }
    }
})

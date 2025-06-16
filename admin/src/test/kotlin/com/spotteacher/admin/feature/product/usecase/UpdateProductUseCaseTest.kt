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
import io.mockk.coVerify
import io.mockk.mockk

class UpdateProductUseCaseTest : DescribeSpec({
    describe("UpdateProductUseCase") {
        // Arrange
        val productRepository = mockk<ProductRepository>()
        val usecase = UpdateProductUseCase(productRepository)
        val productId = ProductId(1)
        val originalProduct = Product(
            id = productId,
            name = ProductName("original name"),
            price = ProductPrice(1000),
            description = ProductDescription("original description")
        )
        val updatedName = ProductName("updated name")
        val updatedPrice = ProductPrice(2000)
        val updatedDescription = ProductDescription("updated description")
        val updatedProduct = Product(
            id = productId,
            name = updatedName,
            price = updatedPrice,
            description = updatedDescription
        )

        describe("call") {
            context("when product exists") {
                it("should update the product and return Success") {
                    // Arrange
                    coEvery { productRepository.findById(productId) } returns originalProduct
                    coEvery { productRepository.update(any()) } returns Unit

                    // Act
                    val result = usecase.call(
                        UpdateProductUseCaseInput(
                            id = productId,
                            name = updatedName,
                            price = updatedPrice,
                            description = updatedDescription
                        )
                    )

                    // Assert
                    result.result shouldBe updatedProduct.right()
                    coVerify { productRepository.update(updatedProduct) }
                }
            }

            context("when product does not exist") {
                it("should return NotFound error") {
                    // Arrange
                    coEvery { productRepository.findById(productId) } returns null

                    // Act
                    val result = usecase.call(
                        UpdateProductUseCaseInput(
                            id = productId,
                            name = updatedName,
                            price = updatedPrice,
                            description = updatedDescription
                        )
                    )

                    // Assert
                    result.result shouldBe ProductError(
                        ProductErrorCode.PRODUCT_NOT_FOUND,
                        "Product not found"
                    ).left()
                    coVerify(exactly = 0) { productRepository.update(any()) }
                }
            }
        }
    }
})

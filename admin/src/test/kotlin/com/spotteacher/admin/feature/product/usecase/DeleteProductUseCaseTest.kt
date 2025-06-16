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

class DeleteProductUseCaseTest : DescribeSpec({
    describe("DeleteProductUseCase") {
        // Arrange
        val productRepository = mockk<ProductRepository>()
        val usecase = DeleteProductUseCase(productRepository)
        val productId = ProductId(1)
        val product = Product(
            id = productId,
            name = ProductName("test product"),
            price = ProductPrice(1000),
            description = ProductDescription("test description")
        )

        describe("call") {
            context("when product exists") {
                it("should delete the product and return Success") {
                    // Arrange
                    coEvery { productRepository.findById(productId) } returns product
                    coEvery { productRepository.delete(productId) } returns Unit

                    // Act
                    val result = usecase.call(
                        DeleteProductUseCaseInput(
                            id = productId
                        )
                    )

                    // Assert
                    result.result shouldBe Unit.right()
                    coVerify { productRepository.delete(productId) }
                }
            }

            context("when product does not exist") {
                it("should return NotFound error") {
                    // Arrange
                    coEvery { productRepository.findById(productId) } returns null

                    // Act
                    val result = usecase.call(
                        DeleteProductUseCaseInput(
                            id = productId
                        )
                    )

                    // Assert
                    result.result shouldBe ProductError(
                        ProductErrorCode.PRODUCT_NOT_FOUND,
                        "Product not found"
                    ).left()
                    coVerify(exactly = 0) { productRepository.delete(any()) }
                }
            }
        }
    }
})

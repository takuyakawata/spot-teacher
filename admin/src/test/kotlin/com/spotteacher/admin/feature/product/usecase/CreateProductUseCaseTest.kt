package com.spotteacher.admin.feature.product.usecase

import com.spotteacher.admin.feature.product.domain.ProductDescription
import com.spotteacher.admin.feature.product.domain.ProductName
import com.spotteacher.admin.feature.product.domain.ProductPrice
import com.spotteacher.admin.feature.product.domain.ProductRepository
import com.spotteacher.admin.fixture.ProductFixture
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk

class CreateProductUseCaseTest : DescribeSpec({
    describe("CreateProductUseCase") {
        // Arrange
        val productRepository = mockk<ProductRepository>()
        val usecase = CreateProductUseCase(productRepository)
        val product = ProductFixture().buildProduct()

        describe("call") {
            context("when input is valid") {
                it("should return success") {
                    // Arrange
                    coEvery { productRepository.create(any()) } returns product

                    // Act
                    val result = usecase.call(
                        CreateProductUseCaseInput(
                            name = ProductName("test"),
                            price = ProductPrice(1000),
                            description = ProductDescription("test description")
                        )
                    )

                    // Assert
                    result shouldBe CreateProductUseCaseOutput.Success(Unit)
                }
            }
        }
    }
})

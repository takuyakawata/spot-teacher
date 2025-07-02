package com.spotteacher.admin.feature.product.usecase

import arrow.core.right
import com.spotteacher.admin.feature.product.domain.Product
import com.spotteacher.admin.feature.product.domain.ProductDescription
import com.spotteacher.admin.feature.product.domain.ProductId
import com.spotteacher.admin.feature.product.domain.ProductName
import com.spotteacher.admin.feature.product.domain.ProductPrice
import com.spotteacher.admin.feature.product.domain.ProductRepository
import com.spotteacher.domain.Pagination
import com.spotteacher.domain.SortOrder
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot

class FindProductsUseCaseTest : DescribeSpec({
    describe("FindProductsUseCase") {
        // Arrange
        val productRepository = mockk<ProductRepository>()
        val usecase = FindProductsUseCase(productRepository)
        val products = listOf(
            Product(
                id = ProductId(1),
                name = ProductName("test product 1"),
                price = ProductPrice(1000),
                description = ProductDescription("test description 1")
            ),
            Product(
                id = ProductId(2),
                name = ProductName("test product 2"),
                price = ProductPrice(2000),
                description = ProductDescription("test description 2")
            ),
            Product(
                id = ProductId(3),
                name = ProductName("test product 3"),
                price = ProductPrice(3000),
                description = ProductDescription("test description 3")
            )
        )

        describe("call") {
            context("when fetching products with limit only") {
                it("should return Success with the products") {
                    // Arrange
                    val paginationSlot = slot<Pagination<Product>>()
                    coEvery { productRepository.getPaginated(capture(paginationSlot)) } returns products.take(2)

                    // Act
                    val result = usecase.call(
                        FindProductsUseCaseInput(
                            limit = 2
                        )
                    )

                    // Assert
                    result.result shouldBe products.take(2).right()
                    paginationSlot.captured.limit shouldBe 2
                    paginationSlot.captured.cursorColumns.isEmpty() shouldBe true
                }
            }

            context("when fetching products with limit and lastId") {
                it("should return Success with the products") {
                    // Arrange
                    val paginationSlot = slot<Pagination<Product>>()
                    coEvery { productRepository.getPaginated(capture(paginationSlot)) } returns products.takeLast(2)

                    // Act
                    val result = usecase.call(
                        FindProductsUseCaseInput(
                            limit = 2,
                            lastId = 1,
                            sortOrder = SortOrder.ASC
                        )
                    )

                    // Assert
                    result.result shouldBe products.takeLast(2).right()
                    paginationSlot.captured.limit shouldBe 2
                    paginationSlot.captured.cursorColumns.size shouldBe 1
                    paginationSlot.captured.cursorColumns[0].order shouldBe SortOrder.ASC
                }
            }
        }
    }
})

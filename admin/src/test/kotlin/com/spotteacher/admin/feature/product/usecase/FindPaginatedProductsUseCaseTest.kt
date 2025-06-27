package com.spotteacher.admin.feature.product.usecase

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

class FindPaginatedProductsUseCaseTest : DescribeSpec({
    describe("FindPaginatedProductsUseCase") {
        // Arrange
        val productRepository = mockk<ProductRepository>()
        val useCase = FindPaginatedProductsUseCase(productRepository)
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
            context("when fetching products with limit only and no lastId") {
                it("should return products with the specified limit") {
                    // Arrange
                    val paginationSlot = slot<Pagination<Product>>()
                    coEvery { productRepository.getPaginated(capture(paginationSlot)) } returns products.take(2)

                    // Act
                    val result = useCase.call(
                        FindPaginatedProductsUseCaseInput(
                            lastId = Pair(null, SortOrder.ASC),
                            limit = 2
                        )
                    )

                    // Assert
                    result shouldBe products.take(2)
                    paginationSlot.captured.limit shouldBe 2
                    paginationSlot.captured.cursorColumns.size shouldBe 1
                }
            }

            context("when fetching products with limit and lastId with ASC order") {
                it("should return products after the lastId with the specified limit") {
                    // Arrange
                    val paginationSlot = slot<Pagination<Product>>()
                    coEvery { productRepository.getPaginated(capture(paginationSlot)) } returns products.takeLast(2)

                    // Act
                    val result = useCase.call(
                        FindPaginatedProductsUseCaseInput(
                            lastId = Pair(ProductId(1), SortOrder.ASC),
                            limit = 2
                        )
                    )

                    // Assert
                    result shouldBe products.takeLast(2)
                    paginationSlot.captured.limit shouldBe 2
                    paginationSlot.captured.cursorColumns.size shouldBe 1
                    paginationSlot.captured.cursorColumns[0].order shouldBe SortOrder.ASC
                    paginationSlot.captured.cursorColumns[0].lastValue shouldBe 1L
                }
            }

            context("when fetching products with limit and lastId with DESC order") {
                it("should return products before the lastId with the specified limit") {
                    // Arrange
                    val paginationSlot = slot<Pagination<Product>>()
                    coEvery { productRepository.getPaginated(capture(paginationSlot)) } returns products.take(2)

                    // Act
                    val result = useCase.call(
                        FindPaginatedProductsUseCaseInput(
                            lastId = Pair(ProductId(3), SortOrder.DESC),
                            limit = 2
                        )
                    )

                    // Assert
                    result shouldBe products.take(2)
                    paginationSlot.captured.limit shouldBe 2
                    paginationSlot.captured.cursorColumns.size shouldBe 1
                    paginationSlot.captured.cursorColumns[0].order shouldBe SortOrder.DESC
                    paginationSlot.captured.cursorColumns[0].lastValue shouldBe 3L
                }
            }

            context("when no products match the criteria") {
                it("should return an empty list") {
                    // Arrange
                    val paginationSlot = slot<Pagination<Product>>()
                    coEvery { productRepository.getPaginated(capture(paginationSlot)) } returns emptyList()

                    // Act
                    val result = useCase.call(
                        FindPaginatedProductsUseCaseInput(
                            lastId = Pair(ProductId(999), SortOrder.ASC),
                            limit = 10
                        )
                    )

                    // Assert
                    result shouldBe emptyList()
                    paginationSlot.captured.limit shouldBe 10
                    paginationSlot.captured.cursorColumns.size shouldBe 1
                    paginationSlot.captured.cursorColumns[0].order shouldBe SortOrder.ASC
                    paginationSlot.captured.cursorColumns[0].lastValue shouldBe 999L
                }
            }
        }
    }
})

package com.spotteacher.admin.feature.product.handler

import com.spotteacher.admin.feature.product.domain.ProductRepository
import com.spotteacher.admin.fixture.ProductFixture
import com.spotteacher.backend.DatabaseDescribeSpec
import graphql.GraphQLContext
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.TimeUnit

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ProductDataLoaderTest(
    private val productRepository: ProductRepository,
    private val productFixture: ProductFixture,
) : DatabaseDescribeSpec({
    describe("UserDataLoader") {
        val dataLoader = ProductDataLoader(productRepository).getDataLoader(GraphQLContext.getDefault())
        describe("load") {
           val product = productFixture.createProduct()

            context("when a valid id is given") {
                it("should return the product") {
                    dataLoader.load(product.id)
                    val result = dataLoader.dispatch().get(10L, TimeUnit.SECONDS)
                    result.size shouldBe 1
                    result[0].id shouldBe product.id.toGraphQLID()
                }
            }

            describe("loadMany") {
                val product2 = productFixture.createProduct()
                val product3 = productFixture.createProduct()


                context("when valid ids are given") {
                    it("should return a list of products") {
                        dataLoader.loadMany(listOf(product2.id, product3.id))
                        val result = dataLoader.dispatch().get(10L, TimeUnit.SECONDS)
                        result.size shouldBe 2
                        result[0].id shouldBe product2.id.toGraphQLID()
                        result[1].id shouldBe product3.id.toGraphQLID()
                    }
                }
            }
        }
    }
})

package com.spotteacher.admin.feature.product.handler

import com.expediagroup.graphql.generator.annotations.GraphQLName
import com.expediagroup.graphql.generator.scalars.ID
import com.spotteacher.admin.feature.product.domain.ProductId
import com.spotteacher.graphql.toID

private const val PRODUCT_TYPE = "Product"

@GraphQLName(PRODUCT_TYPE)
data class ProductType(
    val id: ID,
    val name: String,
    val price: Int,
    val description: String?
)

fun ProductId.toGraphQLID() = this.toID(PRODUCT_TYPE)

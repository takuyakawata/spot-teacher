package com.spotteacher.admin.feature.product.infra

import com.spotteacher.admin.feature.product.domain.Product
import com.spotteacher.admin.feature.product.domain.ProductDescription
import com.spotteacher.admin.feature.product.domain.ProductId
import com.spotteacher.admin.feature.product.domain.ProductName
import com.spotteacher.admin.feature.product.domain.ProductPrice
import com.spotteacher.admin.feature.product.domain.ProductRepository
import com.spotteacher.admin.shared.infra.TransactionAwareDSLContext
import com.spotteacher.domain.Pagination
import com.spotteacher.domain.SortOrder
import com.spotteacher.extension.nonBlockingFetch
import com.spotteacher.extension.nonBlockingFetchOne
import com.spotteacher.infra.db.tables.Products.Companion.PRODUCTS
import com.spotteacher.infra.db.tables.records.ProductsRecord
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitLast
import org.jooq.types.UInteger
import org.jooq.types.ULong
import org.springframework.stereotype.Component

@Component
class ProductRepositoryImpl(private val dslContext: TransactionAwareDSLContext,) : ProductRepository {
    override suspend fun findById(id: ProductId): Product? {
        return dslContext.get().nonBlockingFetchOne(
            PRODUCTS,
            PRODUCTS.ID.eq(ULong.valueOf(id.value))
        )?.toEntity()
    }

    override suspend fun getAll(): List<Product> {
        return dslContext.get().nonBlockingFetch(PRODUCTS).map { it.toEntity() }
    }

    override suspend fun getPaginated(pagination: Pagination<Product>): List<Product> {
        val paginationFields = pagination.cursorColumns.mapNotNull {
            PRODUCTS.field(it.column.name)?.let { column ->
                when (it.order) {
                    SortOrder.ASC -> column.asc()
                    SortOrder.DESC -> column.desc()
                } to it.getPrimitiveValue()
            }
        }
        val query = dslContext.get().selectFrom(PRODUCTS)
            .orderBy(*paginationFields.map { it.first }.toTypedArray())
            .seek(*paginationFields.map { it.second }.toTypedArray())
            .limit(pagination.limit)

        val productFlow = query.asFlow()
            .map { record -> record.into(ProductsRecord::class.java).toEntity() }
        return productFlow.toList()
    }

    override suspend fun create(product: Product):Product {
        val id = dslContext.get().insertInto(
            PRODUCTS,
            PRODUCTS.NAME,
            PRODUCTS.PRICE,
            PRODUCTS.DESCRIPTION,
        ).values(
            product.name.value,
            UInteger.valueOf(product.price.value),
            product.description?.value,
        ).returning(PRODUCTS.ID).awaitFirstOrNull()?.id!!
        return product.copy(id = ProductId(id))
    }

    override suspend fun update(product: Product) {
        dslContext.get().update(PRODUCTS).set(PRODUCTS.NAME, product.name.value)
            .set(PRODUCTS.PRICE, UInteger.valueOf(product.price.value))
            .set(PRODUCTS.DESCRIPTION, product.description?.value)
            .where(PRODUCTS.ID.eq(ULong.valueOf(product.id.value))).awaitLast()
    }

    override suspend fun delete(productId: ProductId) {
        dslContext.get().deleteFrom(PRODUCTS).where(PRODUCTS.ID.eq(ULong.valueOf(productId.value))).awaitLast()
    }

    private fun ProductsRecord.toEntity(): Product {
        return Product(
            id = ProductId(id!!.toLong()),
            name = ProductName(name),
            price = ProductPrice(price.toInt()),
            description = description?.let { ProductDescription(it) },
        )
    }
}

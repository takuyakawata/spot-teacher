package com.spotteacher.admin.feature



data class Product(
    val id: ProductId,
    val name: ProductName,
    val price: ProductPrice,
    val description: ProductDescription?
) {
    companion object {
        fun create(
            name: ProductName,
            price: ProductPrice,
            description: ProductDescription?
        ) = Product(
            id = ProductId(0),
            name = name,
            price = price,
            description = description
        )
    }
}

class ProductId(override val value: Long) : Identity<Long>(value)

@JvmInline
value class ProductName(val value: String) {
    companion object {
        const val MAX_LENGTH = 100
    }
    init {
        require(value.isNotBlank()) {
            "productName must not be blank"
        }
        require(value.length <= MAX_LENGTH) {
            "productName must be less than $MAX_LENGTH"
        }
    }
}

@JvmInline
value class ProductPrice private constructor(val value: Int) {
    companion object {
        operator fun invoke(value: Int) = ProductPrice(value)
    }
    init {
        require(value > 0) {
            "ProductPrice must be plus value"
        }
    }
}

@JvmInline
value class ProductDescription(val value: String) {
    companion object {
        const val MAX_LENGTH = 1000
    }
    init {
        require(value.length <= MAX_LENGTH)
    }
}

data class ProductError(
    val code: ProductErrorCode,
    val message: String
)

enum class ProductErrorCode {
    NAME_DUPLICATED,
}

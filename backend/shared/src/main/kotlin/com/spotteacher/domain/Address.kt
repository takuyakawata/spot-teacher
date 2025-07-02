package com.spotteacher.domain

/**
 * 都道府県
 */
enum class Prefecture {
    HOKKAIDO,
    AOMORI,
    IWATE,
    MIYAGI,
    AKITA,
    YAMAGATA,
    FUKUSHIMA,
    IBARAKI,
    GUNMA,
    TOCHIGI,
    SAITAMA,
    CHIBA,
    TOKYO,
    KANAGAWA,
    NIIGATA,
    TOYAMA,
    ISHIKAWA,
    FUKUI,
    YAMANASHI,
    NAGANO,
    GIFU,
    AICHI,
    MIE,
    SHIGA,
    KYOTO,
    OSAKA,
    HYOGO,
    NARA,
    WAKAYAMA,
    TOTTORI,
    SHIMANE,
    OKAYAMA,
    HIROSHIMA,
    TOKUSHIMA,
    KAGAWA,
    EHIME,
    KOCHI,
    FUKUOKA,
    SAGA,
    NAGASAKI,
    KUMAMOTO,
    OITA,
    MIYAZAKI,
    KAGOSHIMA,
    OKINAWA
}

/**
 * 市区町村
 */
@JvmInline
value class City(val value: String) {
    companion object {
        private const val MAX_LENGTH = 255
    }

    init {
        require(value.isNotBlank()) { "City must not be blank" }
        require(value.length <= MAX_LENGTH) { "City must not be longer than $MAX_LENGTH characters" }
    }
}

/**
 * 番地以下の住所
 */
@JvmInline
value class StreetAddress(val value: String) {
    companion object {
        private const val MAX_LENGTH = 255
    }

    init {
        require(value.length <= MAX_LENGTH) { "Address detail must not be longer than $MAX_LENGTH characters" }
    }
}

/**
 * 建物名
 */
@JvmInline
value class BuildingName(val value: String) {
    companion object {
        private const val MAX_LENGTH = 255
    }

    init {
        require(value.isNotBlank()) { "Building name must not be blank" }
        require(value.length <= MAX_LENGTH) { "Building name must not be longer than $MAX_LENGTH characters" }
    }
}

/**
 * 郵便番号
 */
@JvmInline
value class PostCode(val value: String) {
    companion object {
        private const val MAX_LENGTH = 10
    }
    init {
        require(value.length <= MAX_LENGTH) {
            "Post code must be $MAX_LENGTH digits"
        }
    }
}

/**
 * 住所
 */
data class Address(
    val postCode: PostCode,
    val prefecture: Prefecture,
    val city: City,
    val streetAddress: StreetAddress,
    val buildingName: BuildingName?,
)

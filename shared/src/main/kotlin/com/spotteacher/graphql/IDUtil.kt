package com.spotteacher.graphql


import com.expediagroup.graphql.generator.scalars.ID
import com.spotteacher.admin.shared.util.Identity
import graphql.relay.Relay

fun <T> Identity<T>.toID(type: String) = ID(Relay().toGlobalId(type, this.value.toString()))

fun ID.toLongId() = try {
    Relay().fromGlobalId(this.value).id.toLong()
} catch (e: NumberFormatException) {
    throw IllegalArgumentException("Invalid ID")
}

fun <T : Identity<Long>> ID.toDomainId(mapper: (Long) -> T) = mapper(toLongId())

package com.spotteacher.extension

import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitLast
import org.jooq.Condition
import org.jooq.DSLContext
import org.jooq.Publisher
import org.jooq.TableRecord
import org.jooq.impl.TableImpl
import org.jooq.types.UInteger
import org.jooq.types.ULong
import reactor.core.publisher.Flux

suspend inline fun <reified R : TableRecord<*>, T : TableImpl<R>> DSLContext.nonBlockingFetchOne(
    tableImpl: T,
    vararg condition: Condition,
): R? = this.selectFrom(tableImpl)
    .where(*condition)
    .awaitFirstOrNull()
    ?.map { it.into(R::class.java) }

suspend inline fun <reified R : TableRecord<*>, T : TableImpl<R>> DSLContext.nonBlockingFetch(
    tableImpl: T,
    vararg condition: Condition,
): List<R> = Flux.from(
    this.selectFrom(tableImpl)
        .where(*condition)
).collectList()
    .awaitLast()
    .map { it.into(R::class.java) }

suspend fun <T> Publisher<T>.toList(): List<T> = Flux.from(this).collectList().awaitLast()

fun UInt.toUInteger(): UInteger = UInteger.valueOf(this.toString())

fun Long.toJooqULong(): ULong = ULong.valueOf(this)

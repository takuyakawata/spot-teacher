package com.spotteacher.admin.shared.infra

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.reactor.ReactorContext
import kotlinx.coroutines.reactor.asCoroutineContext
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.reactor.mono
import kotlinx.coroutines.withContext
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.jooq.DSLContext
import org.jooq.kotlin.coroutines.transactionCoroutine
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.util.context.Context
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext
import kotlin.jvm.optionals.getOrNull

@Component
class TransactionCoroutineOperator(private val dslContext: DSLContext) {
    suspend fun <T> execute(block: suspend CoroutineScope.() -> T): T {
        val propagatedDSLContext = coroutineContext.getDslContext()
        return if (propagatedDSLContext != null) {
            execute(propagatedDSLContext, block)
        } else {
            execute(dslContext, block)
        }
    }

    private suspend fun <T> execute(dslContext: DSLContext, block: suspend CoroutineScope.() -> T): T =
        dslContext.transactionCoroutine { config ->
            withContext(coroutineContext.addDslContext(config.dsl()), block)
        }
}

@Component
class TransactionAwareDSLContext(
    @Qualifier("readerDslContext") private val readerDslContext: DSLContext,
) {
    suspend fun get(): DSLContext = coroutineContext.getDslContext() ?: this.readerDslContext
}

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class TransactionCoroutine

@Aspect
@Component
class TransactionCoroutineAspect(private val transactionCoroutineOperator: TransactionCoroutineOperator) {
    @Around("@annotation(com.cosoji.backend.shared.infra.TransactionCoroutine) && args(.., kotlin.coroutines.Continuation)")
    fun executeAnnotatedMethodInTransaction(joinPoint: ProceedingJoinPoint): Mono<*> = mono {
        transactionCoroutineOperator.execute {
            (joinPoint.proceed() as Mono<*>).awaitSingleOrNull()
        }
    }

    @Around("@within(com.cosoji.backend.shared.infra.TransactionCoroutine) && args(.., kotlin.coroutines.Continuation)")
    fun executeMethodWithinAnnotatedClassInTransaction(joinPoint: ProceedingJoinPoint): Mono<*> = mono {
        transactionCoroutineOperator.execute {
            (joinPoint.proceed() as Mono<*>).awaitSingleOrNull()
        }
    }
}

private const val KEY = "org.jooq.DSLContext"

fun CoroutineContext.addDslContext(dslContext: DSLContext): CoroutineContext {
    val reactorContext = this[ReactorContext]
    return if (reactorContext == null) {
        this + ReactorContext(Context.of(KEY, dslContext))
    } else {
        this + reactorContext.context.put(KEY, dslContext).asCoroutineContext()
    }
}

fun CoroutineContext.getDslContext(): DSLContext? {
    return this[ReactorContext]?.context?.getOrEmpty<DSLContext>(KEY)?.getOrNull()
}

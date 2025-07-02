package com.spotteacher.backend.graphql

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.expediagroup.graphql.generator.annotations.GraphQLName
import com.expediagroup.graphql.generator.scalars.ID
import com.expediagroup.graphql.server.operations.Mutation
import com.expediagroup.graphql.server.operations.Query
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.BeanDescription
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializationConfig
import com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.objenesis.ObjenesisStd
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*
import java.util.concurrent.CompletableFuture
import kotlin.reflect.KCallable
import kotlin.reflect.KFunction1
import kotlin.reflect.KFunction2
import kotlin.reflect.KFunction3
import kotlin.reflect.KFunction4
import kotlin.reflect.KSuspendFunction1
import kotlin.reflect.KSuspendFunction2
import kotlin.reflect.KSuspendFunction3
import kotlin.reflect.full.findAnnotation

/**
 * Type-safe GraphQL builder DSL
 */
suspend fun graphQLBuilder(body: suspend GraphQLBuilder.() -> Unit): String {
    return GraphQLBuilder().apply { body() }.buildingQuery.toString()
}

data class GraphQLInput<I>(val name: String, val value: I)

class GraphQLBuilder {

    companion object {
        const val INDENT_SIZE = 2
        val scalarType = setOf(
            String::class,
            Int::class,
            Double::class,
            Float::class,
            Boolean::class,
            ID::class,
            LocalDateTime::class,
            LocalDate::class,
            LocalTime::class,
        )

        val objectMapper = jacksonObjectMapper()
            .registerModules(
                JavaTimeModule(),
                SimpleModule()
                    .addSerializer(
                        Enum::class.java,
                        object : JsonSerializer<Enum<*>>() {
                            override fun serialize(
                                value: Enum<*>,
                                gen: JsonGenerator,
                                serializers: SerializerProvider,
                            ) {
                                gen.writeString(value.name)
                            }
                        }
                    ).setSerializerModifier(
                        object : BeanSerializerModifier() {
                            override fun changeProperties(
                                config: SerializationConfig?,
                                beanDesc: BeanDescription?,
                                beanProperties: MutableList<BeanPropertyWriter>?,
                            ): MutableList<BeanPropertyWriter> {
                                return super.changeProperties(
                                    config,
                                    beanDesc,
                                    beanProperties?.filter {
                                        it.getAnnotation(GraphQLIgnore::class.java) == null
                                    }
                                )
                            }
                        }
                    ),
            ).configure(WRITE_DATES_AS_TIMESTAMPS, false)
    }

    val buildingQuery = StringJoiner("\n")
    var currentIndent = 0

    fun addWithIndent(element: String): StringJoiner {
        return buildingQuery.add(" ".repeat(currentIndent) + element)
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // asQuery without input KFunction1
    @JvmName("asQuery_KFunction1_Query__O__")
    suspend inline fun <Q : Query, reified O : Any> KFunction1<Q, O?>.asQuery(buildOutput: O.() -> Unit) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("query", name, buildOutput)
    }

    @JvmName("asQuery_KFunction1_Query__List_O___")
    suspend inline fun <Q : Query, reified O : Any> KFunction1<Q, List<O?>>.asQuery(buildOutput: O.() -> Unit) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("query", name, buildOutput)
    }

    @JvmName("asQuery_KFunction1_Query__CompletableFuture_O__")
    suspend inline fun <Q : Query, reified O : Any> KFunction1<Q, CompletableFuture<O?>>.asQuery(buildOutput: O.() -> Unit) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("query", name, buildOutput)
    }

    @JvmName("asQuery_KFunction2_Query_____O__")
    suspend inline fun <Q : Query, I : Any, reified O : Any> KFunction2<Q, *, O?>.asQuery(buildOutput: O.() -> Unit) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("query", name, buildOutput)
    }

    @JvmName("asQuery_KFunction2_Query_____List_O___")
    suspend inline fun <Q : Query, I : Any, reified O : Any> KFunction2<Q, *, List<O?>>.asQuery(buildOutput: O.() -> Unit) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("query", name, buildOutput)
    }

    @JvmName("asQuery_KFunction2_Query_____CompletableFuture_O__")
    suspend inline fun <Q : Query, I : Any, reified O : Any> KFunction2<Q, *, CompletableFuture<O?>>.asQuery(buildOutput: O.() -> Unit) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("query", name, buildOutput)
    }

    @JvmName("asQuery_KFunction2_Query__I__O__")
    suspend inline fun <Q : Query, I, reified O : Any> KFunction2<Q, I, O?>.asQuery(
        input: I,
        buildOutput: O.() -> Unit,
    ) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("query", name, GraphQLInput(this.parameters[1].name!!, input), buildOutput)
    }

    @JvmName("asQuery_KFunction2_Query__I__List_O___")
    suspend inline fun <Q : Query, I, reified O : Any> KFunction2<Q, I, List<O?>>.asQuery(
        input: I,
        buildOutput: O.() -> Unit,
    ) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("query", name, GraphQLInput(this.parameters[1].name!!, input), buildOutput)
    }

    @JvmName("asQuery_KFunction2_Query__I__CompletableFuture_O__")
    suspend inline fun <Q : Query, I, reified O : Any> KFunction2<Q, I, CompletableFuture<O?>>.asQuery(
        input: I,
        buildOutput: O.() -> Unit,
    ) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("query", name, GraphQLInput(this.parameters[1].name!!, input), buildOutput)
    }

    @JvmName("asQuery_KFunction3_Query________O__")
    suspend inline fun <Q : Query, reified O : Any> KFunction3<Q, *, *, O>.asQuery(buildOutput: O.() -> Unit) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("query", name, buildOutput)
    }

    @JvmName("asQuery_KFunction3_Query________List_O___")
    suspend inline fun <Q : Query, reified O : Any> KFunction3<Q, *, *, List<O?>>.asQuery(buildOutput: O.() -> Unit) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("query", name, buildOutput)
    }

    @JvmName("asQuery_KFunction3_Query________CompletableFuture_O__")
    suspend inline fun <Q : Query, reified O : Any> KFunction3<Q, *, *, CompletableFuture<O?>>.asQuery(buildOutput: O.() -> Unit) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("query", name, buildOutput)
    }

    @JvmName("asQuery_KFunction3_Query__I_____O__")
    suspend inline fun <Q : Query, I, reified O : Any> KFunction3<Q, I, *, O>.asQuery(
        input: I,
        buildOutput: O.() -> Unit,
    ) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("query", name, GraphQLInput(this.parameters[1].name!!, input), buildOutput)
    }

    @JvmName("asQuery_KFunction3_Query__I_____List_O___")
    suspend inline fun <Q : Query, I, reified O : Any> KFunction3<Q, I, *, List<O?>>.asQuery(
        input: I,
        buildOutput: O.() -> Unit,
    ) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("query", name, GraphQLInput(this.parameters[1].name!!, input), buildOutput)
    }

    @JvmName("asQuery_KFunction3_Query__I_____CompletableFuture_O__")
    suspend inline fun <Q : Query, I, reified O : Any> KFunction3<Q, I, *, CompletableFuture<O?>>.asQuery(
        input: I,
        buildOutput: O.() -> Unit,
    ) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("query", name, GraphQLInput(this.parameters[1].name!!, input), buildOutput)
    }

    @JvmName("asQuery_KFunction3_Query_____I__O__")
    suspend inline fun <Q : Query, I, reified O : Any> KFunction3<Q, *, I, O>.asQuery(
        input: I,
        buildOutput: O.() -> Unit,
    ) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("query", name, GraphQLInput(this.parameters[2].name!!, input), buildOutput)
    }

    @JvmName("asQuery_KFunction3_Query_____I__List_O___")
    suspend inline fun <Q : Query, I, reified O : Any> KFunction3<Q, *, I, List<O?>>.asQuery(
        input: I,
        buildOutput: O.() -> Unit,
    ) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("query", name, GraphQLInput(this.parameters[2].name!!, input), buildOutput)
    }

    @JvmName("asQuery_KFunction3_Query_____I__CompletableFuture_O__")
    suspend inline fun <Q : Query, I, reified O : Any> KFunction3<Q, *, I, CompletableFuture<O?>>.asQuery(
        input: I,
        buildOutput: O.() -> Unit,
    ) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("query", name, GraphQLInput(this.parameters[2].name!!, input), buildOutput)
    }

    // asQuery without input KSuspendFunction1
    @JvmName("asQuery_KSuspendFunction1_Query_O__")
    suspend inline fun <Q : Query, reified O : Any> KSuspendFunction1<Q, O?>.asQuery(buildOutput: O.() -> Unit) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("query", name, buildOutput)
    }

    @JvmName("asQueryList_KSuspendFunction1_Query_List_O__")
    suspend inline fun <Q : Query, reified O : Any> KSuspendFunction1<Q, List<O?>>.asQueryList(buildOutput: O.() -> Unit) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("query", name, buildOutput)
    }

    @JvmName("asQuery_KSuspendFunction1_Query_List_O__")
    @Deprecated("Use asQueryList instead to avoid ambiguity", ReplaceWith("asQueryList(buildOutput)"))
    suspend inline fun <Q : Query, reified O : Any> KSuspendFunction1<Q, List<O?>>.asQuery(buildOutput: O.() -> Unit) {
        asQueryList(buildOutput)
    }

    @JvmName("asQuery_KSuspendFunction2_Query_____O__")
    suspend inline fun <Q : Query, reified O : Any> KSuspendFunction2<Q, *, O?>.asQuery(buildOutput: O.() -> Unit) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("query", name, buildOutput)
    }

    @JvmName("asQuery_KSuspendFunction2_Query_____List_O___")
    suspend inline fun <Q : Query, reified O : Any> KSuspendFunction2<Q, *, List<O?>>.asQuery(buildOutput: O.() -> Unit) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("query", name, buildOutput)
    }

    @JvmName("asQuery_KSuspendFunction2_Query__I__O__")
    suspend inline fun <Q : Query, I, reified O : Any> KSuspendFunction2<Q, I, O?>.asQuery(
        input: I,
        buildOutput: O.() -> Unit,
    ) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("query", name, GraphQLInput(this.parameters[1].name!!, input), buildOutput)
    }

    @JvmName("asQuery_KSuspendFunction2_Query__I__List_O___")
    suspend inline fun <Q : Query, I, reified O : Any> KSuspendFunction2<Q, I, List<O?>>.asQuery(
        input: I,
        buildOutput: O.() -> Unit,
    ) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("query", name, GraphQLInput(this.parameters[1].name!!, input), buildOutput)
    }

    // asQuery without input KSuspendFunction3
    @JvmName("asQuery_KSuspendFunction3_Query________O__")
    suspend inline fun <Q : Query, reified O : Any> KSuspendFunction3<Q, *, *, O>.asQuery(buildOutput: O.() -> Unit) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("query", name, buildOutput)
    }

    @JvmName("asQuery_KSuspendFunction3_Query________List_O___")
    suspend inline fun <Q : Query, reified O : Any> KSuspendFunction3<Q, *, *, List<O?>>.asQuery(buildOutput: O.() -> Unit) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("query", name, buildOutput)
    }

    @JvmName("asQuery_KSuspendFunction3_Query__I_____O__")
    suspend inline fun <Q : Query, I, reified O : Any> KSuspendFunction3<Q, I, *, O>.asQuery(
        input: I,
        buildOutput: O.() -> Unit,
    ) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("query", name, GraphQLInput(this.parameters[1].name!!, input), buildOutput)
    }

    @JvmName("asQuery_KSuspendFunction3_Query__I_____List_O___")
    suspend inline fun <Q : Query, I, reified O : Any> KSuspendFunction3<Q, I, *, List<O?>>.asQueryList(
        input: I,
        buildOutput: O.() -> Unit,
    ) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("query", name, GraphQLInput(this.parameters[1].name!!, input), buildOutput)
    }

    @JvmName("asQuery_KSuspendFunction3_Query_____I__O__")
    suspend inline fun <Q : Query, I, reified O : Any> KSuspendFunction3<Q, *, I, O>.asQuery(
        input: I,
        buildOutput: O.() -> Unit,
    ) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("query", name, GraphQLInput(this.parameters[2].name!!, input), buildOutput)
    }

    @JvmName("asQuery_KSuspendFunction3_Query_____I__List_O___")
    suspend inline fun <Q : Query, I, reified O : Any> KSuspendFunction3<Q, *, I, List<O?>>.asQuery(
        input: I,
        buildOutput: O.() -> Unit,
    ) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("query", name, GraphQLInput(this.parameters[2].name!!, input), buildOutput)
    }

    // mutation input KFunction1
    @JvmName("asMutation_KFunction1_Query__O__")
    suspend inline fun <M : Mutation, reified O : Any> KFunction1<M, O?>.asMutation(buildOutput: O.() -> Unit) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("mutation", name, buildOutput)
    }

    @JvmName("asMutation_KFunction1_Mutation__O__")
    suspend inline fun <M : Mutation, reified O : Any> KFunction1<M, O?>.asMutation() {
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("mutation", name)
    }

    @JvmName("asMutation_KFunction1_Mutation__List_O___")
    suspend inline fun <M : Mutation, reified O : Any> KFunction1<M, List<O?>>.asMutation() {
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("mutation", name)
    }

    @JvmName("asMutation_KFunction1_Mutation__List_O___")
    suspend inline fun <M : Mutation, reified O : Any> KFunction1<M, List<O?>>.asMutation(buildOutput: O.() -> Unit) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("mutation", name, buildOutput)
    }

    @JvmName("asMutation_KFunction1_Mutation__CompletableFuture_O__")
    suspend inline fun <M : Mutation, reified O : Any> KFunction1<M, CompletableFuture<O?>>.asMutation() {
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("mutation", name)
    }

    @JvmName("asMutation_KFunction1_Mutation__CompletableFuture_O__")
    suspend inline fun <M : Mutation, reified O : Any> KFunction1<M, CompletableFuture<O?>>.asMutation(buildOutput: O.() -> Unit) {
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("mutation", name, buildOutput)
    }

    @JvmName("asMutation_KFunction2_Mutation_____O__")
    suspend inline fun <M : Mutation, reified O : Any> KFunction2<M, *, O?>.asMutation() {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("mutation", name)
    }

    @JvmName("asMutation_KFunction2_Mutation_____O__")
    suspend inline fun <M : Mutation, reified O : Any> KFunction2<M, *, O?>.asMutation(buildOutput: O.() -> Unit) {
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("mutation", name, buildOutput)
    }

    @JvmName("asMutation_KFunction2_Mutation_____List_O___")
    suspend inline fun <M : Mutation, reified O : Any> KFunction2<M, *, List<O?>>.asMutation() {
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("mutation", name)
    }

    @JvmName("asMutation_KFunction2_Mutation_____List_O___")
    suspend inline fun <M : Mutation, reified O : Any> KFunction2<M, *, List<O?>>.asMutation(buildOutput: O.() -> Unit) {
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("mutation", name, buildOutput)
    }

    @JvmName("asMutation_KFunction2_Mutation_____CompletableFuture_O__")
    suspend inline fun <M : Mutation, reified O : Any> KFunction2<M, *, CompletableFuture<O?>>.asMutation() {
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("mutation", name)
    }

    @JvmName("asMutation_KFunction2_Mutation_____CompletableFuture_O__")
    suspend inline fun <M : Mutation, reified O : Any> KFunction2<M, *, CompletableFuture<O?>>.asMutation(buildOutput: O.() -> Unit) {
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("mutation", name, buildOutput)
    }

    @JvmName("asMutation_KFunction2_Mutation__I__O__")
    suspend inline fun <M : Mutation, I, reified O : Any> KFunction2<M, I, O?>.asMutation(input: I) {
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("mutation", name, GraphQLInput(this.parameters[1].name!!, input))
    }

    @JvmName("asMutation_KFunction2_Mutation__I__O__")
    suspend inline fun <M : Mutation, I, reified O : Any> KFunction2<M, I, O?>.asMutation(
        input: I,
        buildOutput: O.() -> Unit,
    ) {
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("mutation", name, GraphQLInput(this.parameters[1].name!!, input), buildOutput)
    }

    @JvmName("asMutation_KFunction2_Mutation__I__List_O___")
    suspend inline fun <M : Mutation, I, reified O : Any> KFunction2<M, I, List<O?>>.asMutation(input: I) {
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("mutation", name, GraphQLInput(this.parameters[1].name!!, input))
    }

    @JvmName("asMutation_KFunction2_Mutation__I__List_O___")
    suspend inline fun <M : Mutation, I, reified O : Any> KFunction2<M, I, List<O?>>.asMutation(
        input: I,
        buildOutput: O.() -> Unit,
    ) {
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("mutation", name, GraphQLInput(this.parameters[1].name!!, input), buildOutput)
    }

    @JvmName("asMutation_KFunction2_Mutation__I__CompletableFuture_O__")
    suspend inline fun <M : Mutation, I, reified O : Any> KFunction2<M, I, CompletableFuture<O?>>.asMutation(input: I) {
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("mutation", name, GraphQLInput(this.parameters[1].name!!, input))
    }

    @JvmName("asMutation_KFunction2_Mutation__I__CompletableFuture_O__")
    suspend inline fun <M : Mutation, I, reified O : Any> KFunction2<M, I, CompletableFuture<O?>>.asMutation(
        input: I,
        buildOutput: O.() -> Unit,
    ) {
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("mutation", name, GraphQLInput(this.parameters[1].name!!, input), buildOutput)
    }

    @JvmName("asMutation_KFunction3_Mutation________O__")
    suspend inline fun <M : Mutation, reified O : Any> KFunction3<M, *, *, O>.asMutation() {
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("mutation", name)
    }

    @JvmName("asMutation_KFunction3_Mutation________O__")
    suspend inline fun <M : Mutation, reified O : Any> KFunction3<M, *, *, O>.asMutation(buildOutput: O.() -> Unit) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("mutation", name, buildOutput)
    }

    @JvmName("asMutation_KFunction3_Mutation________List_O___")
    suspend inline fun <M : Mutation, reified O : Any> KFunction3<M, *, *, List<O?>>.asMutation() {
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("mutation", name)
    }

    @JvmName("asMutation_KFunction3_Mutation________List_O___")
    suspend inline fun <M : Mutation, reified O : Any> KFunction3<M, *, *, List<O?>>.asMutation(buildOutput: O.() -> Unit) {
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("mutation", name, buildOutput)
    }

    @JvmName("asMutation_KFunction3_Mutation________CompletableFuture_O__")
    suspend inline fun <M : Mutation, reified O : Any> KFunction3<M, *, *, CompletableFuture<O?>>.asMutation() {
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("mutation", name)
    }

    @JvmName("asMutation_KFunction3_Mutation________CompletableFuture_O__")
    suspend inline fun <M : Mutation, reified O : Any> KFunction3<M, *, *, CompletableFuture<O?>>.asMutation(buildOutput: O.() -> Unit) {
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("mutation", name, buildOutput)
    }

    @JvmName("asMutation_KFunction3_Mutation__I_____O__")
    suspend inline fun <M : Mutation, I, reified O : Any> KFunction3<M, I, *, O>.asMutation(input: I) {
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("mutation", name, GraphQLInput(this.parameters[1].name!!, input))
    }

    @JvmName("asMutation_KFunction3_Mutation__I_____O__")
    suspend inline fun <M : Mutation, I, reified O : Any> KFunction3<M, I, *, O>.asMutation(
        input: I,
        buildOutput: O.() -> Unit,
    ) {
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("mutation", name, GraphQLInput(this.parameters[1].name!!, input), buildOutput)
    }

    @JvmName("asMutation_KFunction3_Mutation__I_____List_O___")
    suspend inline fun <M : Mutation, I, reified O : Any> KFunction3<M, I, *, List<O?>>.asMutation(input: I) {
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("mutation", name, GraphQLInput(this.parameters[1].name!!, input))
    }

    @JvmName("asMutation_KFunction3_Mutation__I_____List_O___")
    suspend inline fun <M : Mutation, I, reified O : Any> KFunction3<M, I, *, List<O?>>.asMutation(
        input: I,
        buildOutput: O.() -> Unit,
    ) {
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("mutation", name, GraphQLInput(this.parameters[1].name!!, input), buildOutput)
    }

    @JvmName("asMutation_KFunction3_Mutation__I_____CompletableFuture_O__")
    suspend inline fun <M : Mutation, I, reified O : Any> KFunction3<M, I, *, CompletableFuture<O?>>.asMutation(input: I) {
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("mutation", name, GraphQLInput(this.parameters[1].name!!, input))
    }

    @JvmName("asMutation_KFunction3_Mutation__I_____CompletableFuture_O__")
    suspend inline fun <M : Mutation, I, reified O : Any> KFunction3<M, I, *, CompletableFuture<O?>>.asMutation(
        input: I,
        buildOutput: O.() -> Unit,
    ) {
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("mutation", name, GraphQLInput(this.parameters[1].name!!, input), buildOutput)
    }

    @JvmName("asMutation_KFunction3_Mutation_____I__O__")
    suspend inline fun <M : Mutation, I, reified O : Any> KFunction3<M, *, I, O>.asMutation(input: I) {
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("mutation", name, GraphQLInput(this.parameters[2].name!!, input))
    }

    @JvmName("asMutation_KFunction3_Mutation_____I__O__")
    suspend inline fun <M : Mutation, I, reified O : Any> KFunction3<M, *, I, O>.asMutation(
        input: I,
        buildOutput: O.() -> Unit,
    ) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("mutation", name, GraphQLInput(this.parameters[2].name!!, input), buildOutput)
    }

    @JvmName("asMutation_KFunction3_Mutation_____I__List_O___")
    suspend inline fun <M : Mutation, I, reified O : Any> KFunction3<M, *, I, List<O?>>.asMutation(input: I) {
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("mutation", name, GraphQLInput(this.parameters[2].name!!, input))
    }

    @JvmName("asMutation_KFunction3_Mutation_____I__List_O___")
    suspend inline fun <M : Mutation, I, reified O : Any> KFunction3<M, *, I, List<O?>>.asMutation(
        input: I,
        buildOutput: O.() -> Unit,
    ) {
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("mutation", name, GraphQLInput(this.parameters[2].name!!, input), buildOutput)
    }

    @JvmName("asMutation_KFunction3_Mutation_____I__CompletableFuture_O__")
    suspend inline fun <M : Mutation, I, reified O : Any> KFunction3<M, *, I, CompletableFuture<O?>>.asMutation(input: I) {
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("mutation", name, GraphQLInput(this.parameters[2].name!!, input))
    }

    @JvmName("asMutation_KFunction3_Mutation_____I__CompletableFuture_O__")
    suspend inline fun <M : Mutation, I, reified O : Any> KFunction3<M, *, I, CompletableFuture<O?>>.asMutation(
        input: I,
        buildOutput: O.() -> Unit,
    ) {
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("mutation", name, GraphQLInput(this.parameters[2].name!!, input), buildOutput)
    }

    // mutation input KSuspendFunction1
    @JvmName("asMutation_KSuspendFunction1_Mutation__O__")
    suspend inline fun <M : Mutation, reified O : Any> KSuspendFunction1<M, O?>.asMutation(buildOutput: O.() -> Unit) {
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("mutation", name, buildOutput)
    }

    @JvmName("asMutation_KSuspendFunction1_Mutation__O__")
    suspend inline fun <M : Mutation, reified O : Any> KSuspendFunction1<M, O?>.asMutation() {
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("mutation", name)
    }

    @JvmName("asMutation_KSuspendFunction1_Mutation__List_O___")
    suspend inline fun <M : Mutation, reified O : Any> KSuspendFunction1<M, List<O?>>.asMutation() {
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("mutation", name)
    }

    @JvmName("asMutation_KSuspendFunction1_Mutation__List_O___")
    suspend inline fun <M : Mutation, reified O : Any> KSuspendFunction1<M, List<O?>>.asMutation(buildOutput: O.() -> Unit) {
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("mutation", name, buildOutput)
    }

    @JvmName("asMutation_KSuspendFunction2_Mutation_____O__")
    suspend inline fun <M : Mutation, reified O : Any> KSuspendFunction2<M, *, O?>.asMutation() {
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("mutation", name)
    }

    @JvmName("asMutation_KSuspendFunction2_Mutation_____O__")
    suspend inline fun <M : Mutation, reified O : Any> KSuspendFunction2<M, *, O?>.asMutation(buildOutput: O.() -> Unit) {
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("mutation", name, buildOutput)
    }

    @JvmName("asMutation_KSuspendFunction2_Mutation_____List_O___")
    suspend inline fun <M : Mutation, reified O : Any> KSuspendFunction2<M, *, List<O?>>.asMutation() {
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("mutation", name)
    }

    @JvmName("asMutation_KSuspendFunction2_Mutation_____List_O___")
    suspend inline fun <M : Mutation, reified O : Any> KSuspendFunction2<M, *, List<O?>>.asMutation(buildOutput: O.() -> Unit) {
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("mutation", name, buildOutput)
    }

    @JvmName("asMutation_KSuspendFunction2_Mutation__I__O__")
    suspend inline fun <M : Mutation, I, reified O : Any> KSuspendFunction2<M, I, O?>.asMutation(input: I) {
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("mutation", name, GraphQLInput(this.parameters[1].name!!, input))
    }

    @JvmName("asMutation_KSuspendFunction2_Mutation__I__O__")
    suspend inline fun <M : Mutation, I, reified O : Any> KSuspendFunction2<M, I, O?>.asMutation(
        input: I,
        buildOutput: O.() -> Unit,
    ) {
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("mutation", name, GraphQLInput(this.parameters[1].name!!, input), buildOutput)
    }

    @JvmName("asMutation_KSuspendFunction2_Mutation__I__List_O___")
    suspend inline fun <M : Mutation, I, reified O : Any> KSuspendFunction2<M, I, List<O?>>.asMutation(input: I) {
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("mutation", name, GraphQLInput(this.parameters[1].name!!, input))
    }

    @JvmName("asMutation_KSuspendFunction2_Mutation__I__List_O___")
    suspend inline fun <M : Mutation, I, reified O : Any> KSuspendFunction2<M, I, List<O?>>.asMutation(
        input: I,
        buildOutput: O.() -> Unit,
    ) {
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("mutation", name, GraphQLInput(this.parameters[1].name!!, input), buildOutput)
    }

    @JvmName("asMutation_KSuspendFunction3_Mutation________O__")
    suspend inline fun <M : Mutation, reified O : Any> KSuspendFunction3<M, *, *, O>.asMutation() {
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("mutation", name)
    }

    @JvmName("asMutation_KSuspendFunction3_Mutation________O__")
    suspend inline fun <M : Mutation, reified O : Any> KSuspendFunction3<M, *, *, O>.asMutation(buildOutput: O.() -> Unit) {
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("mutation", name, buildOutput)
    }

    @JvmName("asMutation_KSuspendFunction3_Mutation________List_O___")
    suspend inline fun <M : Mutation, reified O : Any> KSuspendFunction3<M, *, *, List<O?>>.asMutation() {
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("mutation", name)
    }

    @JvmName("asMutation_KSuspendFunction3_Mutation________List_O___")
    suspend inline fun <M : Mutation, reified O : Any> KSuspendFunction3<M, *, *, List<O?>>.asMutation(buildOutput: O.() -> Unit) {
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("mutation", name, buildOutput)
    }

    @JvmName("asMutation_KSuspendFunction3_Mutation__I_____O__")
    suspend inline fun <M : Mutation, I, reified O : Any> KSuspendFunction3<M, I, *, O>.asMutation(input: I) {
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("mutation", name, GraphQLInput(this.parameters[1].name!!, input))
    }

    @JvmName("asMutation_KSuspendFunction3_Mutation__I_____O__")
    suspend inline fun <M : Mutation, I, reified O : Any> KSuspendFunction3<M, I, *, O>.asMutation(
        input: I,
        buildOutput: O.() -> Unit,
    ) {
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("mutation", name, GraphQLInput(this.parameters[1].name!!, input), buildOutput)
    }

    @JvmName("asMutation_KSuspendFunction3_Mutation__I_____List_O___")
    suspend inline fun <M : Mutation, I, reified O : Any> KSuspendFunction3<M, I, *, List<O?>>.asMutation(input: I) {
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("mutation", name, GraphQLInput(this.parameters[1].name!!, input))
    }

    @JvmName("asMutation_KSuspendFunction3_Mutation__I_____List_O___")
    suspend inline fun <M : Mutation, I, reified O : Any> KSuspendFunction3<M, I, *, List<O?>>.asMutationList(
        input: I,
        buildOutput: O.() -> Unit,
    ) {
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("mutation", name, GraphQLInput(this.parameters[1].name!!, input), buildOutput)
    }

    @JvmName("asMutation_KSuspendFunction3_Mutation_____I__O__")
    suspend inline fun <M : Mutation, I, reified O : Any> KSuspendFunction3<M, *, I, O>.asMutation(input: I) {
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("mutation", name, GraphQLInput(this.parameters[2].name!!, input))
    }

    @JvmName("asMutation_KSuspendFunction3_Mutation_____I__O__")
    suspend inline fun <M : Mutation, I, reified O : Any> KSuspendFunction3<M, *, I, O>.asMutation(
        input: I,
        buildOutput: O.() -> Unit,
    ) {
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("mutation", name, GraphQLInput(this.parameters[2].name!!, input), buildOutput)
    }

    @JvmName("asMutation_KSuspendFunction3_Mutation_____I__List_O___")
    suspend inline fun <M : Mutation, I, reified O : Any> KSuspendFunction3<M, *, I, List<O?>>.asMutation(input: I) {
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("mutation", name, GraphQLInput(this.parameters[2].name!!, input))
    }

    @JvmName("asMutation_KSuspendFunction3_Mutation_____I__List_O___")
    suspend inline fun <M : Mutation, I, reified O : Any> KSuspendFunction3<M, *, I, List<O?>>.asMutation(
        input: I,
        buildOutput: O.() -> Unit,
    ) {
        val name = this.annotations.filterIsInstance<GraphQLName>().firstOrNull()?.value ?: this.name
        handleQuery("mutation", name, GraphQLInput(this.parameters[2].name!!, input), buildOutput)
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // asField without input
    @JvmName("asField_KCallable_Enum_O__")
    suspend inline fun <reified O : Enum<O>> KCallable<O?>.asField() {
        require(O::class !in scalarType && O::class.java.isEnum) { "${O::class.simpleName} seems enum type" }
        handleField(name)
    }

    @JvmName("asField_KCallable_O__")
    suspend inline fun <reified O : Any> KCallable<O?>.asField() {
        require(O::class in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleField(name)
    }

    @JvmName("asField_KCallable_List_O__")
    suspend inline fun <reified O : Any> KCallable<List<O?>>.asFields() {
        require(O::class in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleField(name)
    }

    @JvmName("asField_KCallable_List_O___")
    suspend inline fun <reified O : Any> KCallable<List<O>>.asFields() {
        require(O::class in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleField(name)
    }

    @JvmName("asField_KCallable_CompletableFuture_O__")
    suspend inline fun <reified O : Any> KCallable<CompletableFuture<O?>>.asFieldLazy() {
        require(O::class in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleField(name)
    }

    @JvmName("asField_KCallable_CompletableFuture_List_O__")
    suspend inline fun <reified O : Any> KCallable<CompletableFuture<List<O?>>>.asFieldsLazy() {
        require(O::class in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleField(name)
    }

    @JvmName("asField_KCallable_CompletableFuture_List_O___")
    suspend inline fun <reified O : Any> KCallable<CompletableFuture<List<O>>>.asFieldsLazy() {
        require(O::class in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleField(name)
    }

    // asField with input
    @JvmName("asField_KFunction1_I__O__")
    suspend inline fun <I, reified O : Any> KFunction1<I, O?>.asField(input: I) {
        require(O::class in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleField(name, GraphQLInput(this.parameters[0].name!!, input))
    }

    @JvmName("asField_KFunction1_I__List_O___")
    suspend inline fun <I, reified O : Any> KFunction1<I, List<O?>>.asFields(input: I) {
        require(O::class in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleField(name, GraphQLInput(this.parameters[0].name!!, input))
    }

    @JvmName("asField_KFunction1_I__List_O__")
    suspend inline fun <I, reified O : Any> KFunction1<I, List<O>>.asFields(input: I) {
        require(O::class in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleField(name, GraphQLInput(this.parameters[0].name!!, input))
    }

    @JvmName("asField_KFunction1_I__CompletableFuture_O___")
    suspend inline fun <I, reified O : Any> KFunction1<I, CompletableFuture<O?>>.asFieldLazy(input: I) {
        require(O::class in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleField(name, GraphQLInput(this.parameters[0].name!!, input))
    }

    @JvmName("asField_KFunction1_I__CompletableFuture_O__")
    suspend inline fun <I, reified O : Any> KFunction1<I, CompletableFuture<O>>.asFieldLazy(input: I) {
        require(O::class in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleField(name, GraphQLInput(this.parameters[0].name!!, input))
    }

    @JvmName("asField_KFunction1_I__CompletableFuture_List_O___")
    suspend inline fun <I, reified O : Any> KFunction1<I, CompletableFuture<List<O?>>>.asFieldsLazy(input: I) {
        require(O::class in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleField(name, GraphQLInput(this.parameters[0].name!!, input))
    }

    @JvmName("asField_KFunction1_I__CompletableFuture_List_O__")
    suspend inline fun <I, reified O : Any> KFunction1<I, CompletableFuture<List<O>>>.asFieldsLazy(input: I) {
        require(O::class in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleField(name, GraphQLInput(this.parameters[0].name!!, input))
    }

    @JvmName("asField_KFunction2_I_____O__")
    suspend inline fun <I, reified O : Any> KFunction2<I, *, O?>.asField(input: I) {
        require(O::class in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleField(name, GraphQLInput(this.parameters[0].name!!, input))
    }

    @JvmName("asField_KFunction2_I_____List_O___")
    suspend inline fun <I, reified O : Any> KFunction2<I, *, List<O?>>.asFields(input: I) {
        require(O::class in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleField(name, GraphQLInput(this.parameters[0].name!!, input))
    }

    @JvmName("asField_KFunction2_I_____List_O__")
    suspend inline fun <I, reified O : Any> KFunction2<I, *, List<O>>.asFields(input: I) {
        require(O::class in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleField(name, GraphQLInput(this.parameters[0].name!!, input))
    }

    @JvmName("asField_KFunction2_I_____CompletableFuture_O___")
    suspend inline fun <I, reified O : Any> KFunction2<I, *, CompletableFuture<O?>>.asFieldLazy(input: I) {
        require(O::class in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleField(name, GraphQLInput(this.parameters[0].name!!, input))
    }

    @JvmName("asField_KFunction2_I_____CompletableFuture_O__")
    suspend inline fun <I, reified O : Any> KFunction2<I, *, CompletableFuture<O>>.asFieldLazy(input: I) {
        require(O::class in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleField(name, GraphQLInput(this.parameters[0].name!!, input))
    }

    @JvmName("asField_KFunction2_I_____CompletableFuture_List_O___")
    suspend inline fun <I, reified O : Any> KFunction2<I, *, CompletableFuture<List<O?>>>.asFieldsLazy(input: I) {
        require(O::class in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleField(name, GraphQLInput(this.parameters[0].name!!, input))
    }

    @JvmName("asField_KFunction2_I_____CompletableFuture_List_O__")
    suspend inline fun <I, reified O : Any> KFunction2<I, *, CompletableFuture<List<O>>>.asFieldsLazy(input: I) {
        require(O::class in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleField(name, GraphQLInput(this.parameters[0].name!!, input))
    }

    @JvmName("asField_KFunction2____I__O__")
    suspend inline fun <I, reified O : Any> KFunction2<*, I, O?>.asField(input: I) {
        require(O::class in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleField(name, GraphQLInput(this.parameters[1].name!!, input))
    }

    @JvmName("asField_KFunction2____I__List_O___")
    suspend inline fun <I, reified O : Any> KFunction2<*, I, List<O?>>.asFields(input: I) {
        require(O::class in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleField(name, GraphQLInput(this.parameters[1].name!!, input))
    }

    @JvmName("asField_KFunction2____I__List_O__")
    suspend inline fun <I, reified O : Any> KFunction2<*, I, List<O>>.asFields(input: I) {
        require(O::class in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleField(name, GraphQLInput(this.parameters[1].name!!, input))
    }

    @JvmName("asField_KFunction2____I__CompletableFuture_O___")
    suspend inline fun <I, reified O : Any> KFunction2<*, I, CompletableFuture<O?>>.asFieldLazy(input: I) {
        require(O::class in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleField(name, GraphQLInput(this.parameters[1].name!!, input))
    }

    @JvmName("asField_KFunction2____I__CompletableFuture_O__")
    suspend inline fun <I, reified O : Any> KFunction2<*, I, CompletableFuture<O>>.asFieldLazy(input: I) {
        require(O::class in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleField(name, GraphQLInput(this.parameters[1].name!!, input))
    }

    @JvmName("asField_KFunction2____I__CompletableFuture_List_O___")
    suspend inline fun <I, reified O : Any> KFunction2<*, I, CompletableFuture<List<O?>>>.asFieldsLazy(input: I) {
        require(O::class in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleField(name, GraphQLInput(this.parameters[1].name!!, input))
    }

    @JvmName("asField_KFunction2____I__CompletableFuture_List_O__")
    suspend inline fun <I, reified O : Any> KFunction2<*, I, CompletableFuture<List<O>>>.asFieldsLazy(input: I) {
        require(O::class in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleField(name, GraphQLInput(this.parameters[1].name!!, input))
    }

    @JvmName("asField_KFunction3_I________O__")
    suspend inline fun <I, reified O : Any> KFunction3<I, *, *, O?>.asField(input: I) {
        require(O::class in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleField(name, GraphQLInput(this.parameters[0].name!!, input))
    }

    @JvmName("asField_KFunction3_I________List_O___")
    suspend inline fun <I, reified O : Any> KFunction3<I, *, *, List<O?>>.asField(input: I) {
        require(O::class in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleField(name, GraphQLInput(this.parameters[0].name!!, input))
    }

    @JvmName("asField_KFunction3_I________List_O__")
    suspend inline fun <I, reified O : Any> KFunction3<I, *, *, List<O>>.asField(input: I) {
        require(O::class in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleField(name, GraphQLInput(this.parameters[0].name!!, input))
    }

    @JvmName("asField_KFunction3_I________CompletableFuture_O___")
    suspend inline fun <I, reified O : Any> KFunction3<I, *, *, CompletableFuture<O?>>.asFieldLazy(input: I) {
        require(O::class in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleField(name, GraphQLInput(this.parameters[0].name!!, input))
    }

    @JvmName("asField_KFunction3_I________CompletableFuture_O__")
    suspend inline fun <I, reified O : Any> KFunction3<I, *, *, CompletableFuture<O>>.asFieldLazy(input: I) {
        require(O::class in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleField(name, GraphQLInput(this.parameters[0].name!!, input))
    }

    @JvmName("asField_KFunction3_I________CompletableFuture_List_O___")
    suspend inline fun <I, reified O : Any> KFunction3<I, *, *, CompletableFuture<List<O?>>>.asFieldLazy(input: I) {
        require(O::class in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleField(name, GraphQLInput(this.parameters[0].name!!, input))
    }

    @JvmName("asField_KFunction3_I________CompletableFuture_List_O__")
    suspend inline fun <I, reified O : Any> KFunction3<I, *, *, CompletableFuture<List<O>>>.asFieldLazy(input: I) {
        require(O::class in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleField(name, GraphQLInput(this.parameters[0].name!!, input))
    }

    @JvmName("asField_KFunction3____I_____O__")
    suspend inline fun <I, reified O : Any> KFunction3<*, I, *, O?>.asField(input: I) {
        require(O::class in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleField(name, GraphQLInput(this.parameters[1].name!!, input))
    }

    @JvmName("asField_KFunction3____I_____List_O___")
    suspend inline fun <I, reified O : Any> KFunction3<*, I, *, List<O?>>.asField(input: I) {
        require(O::class in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleField(name, GraphQLInput(this.parameters[1].name!!, input))
    }

    @JvmName("asField_KFunction3____I_____List_O__")
    suspend inline fun <I, reified O : Any> KFunction3<*, I, *, List<O>>.asField(input: I) {
        require(O::class in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleField(name, GraphQLInput(this.parameters[1].name!!, input))
    }

    @JvmName("asField_KFunction3____I_____CompletableFuture_O___")
    suspend inline fun <I, reified O : Any> KFunction3<*, I, *, CompletableFuture<O?>>.asFieldLazy(input: I) {
        require(O::class in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleField(name, GraphQLInput(this.parameters[1].name!!, input))
    }

    @JvmName("asField_KFunction3____I_____CompletableFuture_O__")
    suspend inline fun <I, reified O : Any> KFunction3<*, I, *, CompletableFuture<O>>.asFieldLazy(input: I) {
        require(O::class in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleField(name, GraphQLInput(this.parameters[1].name!!, input))
    }

    @JvmName("asField_KFunction3____I_____CompletableFuture_List_O___")
    suspend inline fun <I, reified O : Any> KFunction3<*, I, *, CompletableFuture<List<O?>>>.asFieldLazy(input: I) {
        require(O::class in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleField(name, GraphQLInput(this.parameters[1].name!!, input))
    }

    @JvmName("asField_KFunction3____I_____CompletableFuture_List_O__")
    suspend inline fun <I, reified O : Any> KFunction3<*, I, *, CompletableFuture<List<O>>>.asFieldLazy(input: I) {
        require(O::class in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleField(name, GraphQLInput(this.parameters[1].name!!, input))
    }

    @JvmName("asField_KFunction3_______I__O__")
    suspend inline fun <I, reified O : Any> KFunction3<*, *, I, O?>.asField(input: I) {
        require(O::class in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleField(name, GraphQLInput(this.parameters[2].name!!, input))
    }

    @JvmName("asField_KFunction3_______I__List_O___")
    suspend inline fun <I, reified O : Any> KFunction3<*, *, I, List<O?>>.asField(input: I) {
        require(O::class in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleField(name, GraphQLInput(this.parameters[2].name!!, input))
    }

    @JvmName("asField_KFunction3_______I__List_O__")
    suspend inline fun <I, reified O : Any> KFunction3<*, *, I, List<O>>.asField(input: I) {
        require(O::class in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleField(name, GraphQLInput(this.parameters[2].name!!, input))
    }

    @JvmName("asField_KFunction3_______I__CompletableFuture_O___")
    suspend inline fun <I, reified O : Any> KFunction3<*, *, I, CompletableFuture<O?>>.asFieldLazy(input: I) {
        require(O::class in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleField(name, GraphQLInput(this.parameters[2].name!!, input))
    }

    @JvmName("asField_KFunction3_______I__CompletableFuture_O__")
    suspend inline fun <I, reified O : Any> KFunction3<*, *, I, CompletableFuture<O>>.asFieldLazy(input: I) {
        require(O::class in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleField(name, GraphQLInput(this.parameters[2].name!!, input))
    }

    @JvmName("asField_KFunction3_______I__CompletableFuture_List_O___")
    suspend inline fun <I, reified O : Any> KFunction3<*, *, I, CompletableFuture<List<O?>>>.asFieldLazy(input: I) {
        require(O::class in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleField(name, GraphQLInput(this.parameters[2].name!!, input))
    }

    @JvmName("asField_KFunction3_______I__CompletableFuture_List_O__")
    suspend inline fun <I, reified O : Any> KFunction3<*, *, I, CompletableFuture<List<O>>>.asFieldLazy(input: I) {
        require(O::class in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleField(name, GraphQLInput(this.parameters[2].name!!, input))
    }

    @JvmName("asField_KSuspendFunction1_I__O__")
    suspend inline fun <I, reified O : Any> KSuspendFunction1<I, O?>.asField(input: I) {
        require(O::class in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleField(name)
    }

    @JvmName("asField_KSuspendFunction1_I__List_O___")
    suspend inline fun <I, reified O : Any> KSuspendFunction1<I, List<O?>>.asField(input: I) {
        require(O::class in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleField(name)
    }

    @JvmName("asField_KSuspendFunction1_I__List_O__")
    suspend inline fun <I, reified O : Any> KSuspendFunction1<I, List<O>>.asField(input: I) {
        require(O::class in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleField(name)
    }

    @JvmName("asField_KSuspendFunction2_I_____O___")
    suspend inline fun <I, reified O : Any> KSuspendFunction2<I, *, O?>.asField(input: I) {
        require(O::class in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleField(name, GraphQLInput(this.parameters[0].name!!, input))
    }

    @JvmName("asField_KSuspendFunction2_I_____List_O___")
    suspend inline fun <I, reified O : Any> KSuspendFunction2<I, *, List<O?>>.asField(input: I) {
        require(O::class in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleField(name, GraphQLInput(this.parameters[0].name!!, input))
    }

    @JvmName("asField_KSuspendFunction2____I__O__")
    suspend inline fun <I, reified O : Any> KSuspendFunction2<*, I, O?>.asField(input: I) {
        require(O::class in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleField(name, GraphQLInput(this.parameters[1].name!!, input))
    }

    @JvmName("asField_KSuspendFunction2____I__List_O___")
    suspend inline fun <I, reified O : Any> KSuspendFunction2<*, I, List<O?>>.asField(input: I) {
        require(O::class in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleField(name, GraphQLInput(this.parameters[1].name!!, input))
    }

    @JvmName("asField_KSuspendFunction3_I________O__")
    suspend inline fun <I, reified O : Any> KSuspendFunction3<I, *, *, O?>.asField(input: I) {
        require(O::class in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleField(name, GraphQLInput(this.parameters[0].name!!, input))
    }

    @JvmName("asField_KSuspendFunction3_I________List_O___")
    suspend inline fun <I, reified O : Any> KSuspendFunction3<I, *, *, List<O?>>.asField(input: I) {
        require(O::class in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleField(name, GraphQLInput(this.parameters[0].name!!, input))
    }

    @JvmName("asField_KSuspendFunction3____I_____O__")
    suspend inline fun <I, reified O : Any> KSuspendFunction3<*, I, *, O?>.asField(input: I) {
        require(O::class in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleField(name, GraphQLInput(this.parameters[1].name!!, input))
    }

    @JvmName("asField_KSuspendFunction3____I_____List_O___")
    suspend inline fun <I, reified O : Any> KSuspendFunction3<*, I, *, List<O?>>.asField(input: I) {
        require(O::class in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleField(name, GraphQLInput(this.parameters[1].name!!, input))
    }

    @JvmName("asField_KSuspendFunction3_______I__O__")
    suspend inline fun <I, reified O : Any> KSuspendFunction3<*, *, I, O?>.asField(input: I) {
        require(O::class in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleField(name, GraphQLInput(this.parameters[2].name!!, input))
    }

    @JvmName("asField_KSuspendFunction3_______I__List_O___")
    suspend inline fun <I, reified O : Any> KSuspendFunction3<*, *, I, List<O?>>.asField(input: I) {
        require(O::class in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleField(name, GraphQLInput(this.parameters[2].name!!, input))
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // asObject
    @JvmName("asObject_KCallable_O__")
    suspend inline fun <reified O : Any> KCallable<O?>.asObject(buildOutput: O.() -> Unit) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleObject(name, buildOutput)
    }

    @JvmName("asObject_KCallable_List_O___")
    suspend inline fun <reified O : Any> KCallable<List<O?>>.asObjectList(buildOutput: O.() -> Unit) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleObject(name, buildOutput)
    }

    @JvmName("asObject_KCallable_List_O__")
    suspend inline fun <reified O : Any> KCallable<List<O>>.asObjectList(buildOutput: O.() -> Unit) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleObject(name, buildOutput)
    }

    @JvmName("asObject_KCallable_CompletableFuture_O__")
    suspend inline fun <reified O : Any> KCallable<CompletableFuture<O>>.asObjectLazy(buildOutput: O.() -> Unit) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleObject(name, buildOutput)
    }

    @JvmName("asObject_KCallable_CompletableFuture_List_O___")
    suspend inline fun <reified O : Any> KCallable<CompletableFuture<List<O?>>>.asObjectListLazy(buildOutput: O.() -> Unit) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleObject(name, buildOutput)
    }

    @JvmName("asObject_KFunction1_I_O__")
    suspend inline fun <I, reified O : Any> KFunction1<I, O?>.asObject(input: I, buildOutput: O.() -> Unit) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleObject(name, GraphQLInput(this.parameters[0].name!!, input), buildOutput)
    }

    @JvmName("asObject_KFunction1_I_List_O___")
    suspend inline fun <I, reified O : Any> KFunction1<I, List<O?>>.asObject(input: I, buildOutput: O.() -> Unit) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleObject(name, GraphQLInput(this.parameters[0].name!!, input), buildOutput)
    }

    @JvmName("asObject_KFunction1_I_List_O__")
    suspend inline fun <I, reified O : Any> KFunction1<I, List<O>>.asObject(input: I, buildOutput: O.() -> Unit) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleObject(name, GraphQLInput(this.parameters[0].name!!, input), buildOutput)
    }

    @JvmName("asObject_KFunction1_I_CompletableFuture_O__")
    suspend inline fun <I, reified O : Any> KFunction1<I, CompletableFuture<O>>.asObjectLazy(
        input: I,
        buildOutput: O.() -> Unit,
    ) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleObject(name, GraphQLInput(this.parameters[0].name!!, input), buildOutput)
    }

    @JvmName("asObject_KFunction1_I_CompletableFuture_O___")
    suspend inline fun <I, reified O : Any> KFunction1<I, CompletableFuture<O?>>.asObjectLazy(
        input: I,
        buildOutput: O.() -> Unit,
    ) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleObject(name, GraphQLInput(this.parameters[0].name!!, input), buildOutput)
    }

    @JvmName("asObject_KFunction1_I_CompletableFuture_List_O___")
    suspend inline fun <I, reified O : Any> KFunction1<I, CompletableFuture<List<O?>>>.asObjectLazy(
        input: I,
        buildOutput: O.() -> Unit,
    ) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleObject(name, GraphQLInput(this.parameters[0].name!!, input), buildOutput)
    }

    @JvmName("asObject_KFunction1_I_CompletableFuture_List_O__")
    suspend inline fun <I, reified O : Any> KFunction1<I, CompletableFuture<List<O>>>.asObjectLazy(
        input: I,
        buildOutput: O.() -> Unit,
    ) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleObject(name, GraphQLInput(this.parameters[0].name!!, input), buildOutput)
    }

    @JvmName("asObject_KFunction2_I_____O__")
    suspend inline fun <I, reified O : Any> KFunction2<I, *, O?>.asObject(input: I, buildOutput: O.() -> Unit) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleObject(name, GraphQLInput(this.parameters[0].name!!, input), buildOutput)
    }

    @JvmName("asObject_KFunction2_I_____List_O___")
    suspend inline fun <I, reified O : Any> KFunction2<I, *, List<O?>>.asObject(input: I, buildOutput: O.() -> Unit) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleObject(name, GraphQLInput(this.parameters[0].name!!, input), buildOutput)
    }

    @JvmName("asObject_KFunction2_I_____List_O__")
    suspend inline fun <I, reified O : Any> KFunction2<I, *, List<O>>.asObject(input: I, buildOutput: O.() -> Unit) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleObject(name, GraphQLInput(this.parameters[0].name!!, input), buildOutput)
    }

    @JvmName("asObject_KFunction2_I_____CompletableFuture_O__")
    suspend inline fun <I, reified O : Any> KFunction2<I, *, CompletableFuture<O>>.asObjectLazy(
        input: I,
        buildOutput: O.() -> Unit,
    ) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleObject(name, GraphQLInput(this.parameters[0].name!!, input), buildOutput)
    }

    @JvmName("asObject_KFunction2_I_____CompletableFuture_O___")
    suspend inline fun <I, reified O : Any> KFunction2<I, *, CompletableFuture<O?>>.asObjectLazy(
        input: I,
        buildOutput: O.() -> Unit,
    ) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleObject(name, GraphQLInput(this.parameters[0].name!!, input), buildOutput)
    }

    @JvmName("asObject_KFunction2_I_____CompletableFuture_List_O__")
    suspend inline fun <I, reified O : Any> KFunction2<I, *, CompletableFuture<List<O>>>.asObjectLazy(
        input: I,
        buildOutput: O.() -> Unit,
    ) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleObject(name, GraphQLInput(this.parameters[0].name!!, input), buildOutput)
    }

    @JvmName("asObject_KFunction2____I__O__")
    suspend inline fun <I, reified O : Any> KFunction2<*, I, O?>.asObject(input: I, buildOutput: O.() -> Unit) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleObject(name, GraphQLInput(this.parameters[1].name!!, input), buildOutput)
    }

    @JvmName("asObject_KFunction2____I__List_O___")
    suspend inline fun <I, reified O : Any> KFunction2<*, I, List<O?>>.asObject(input: I, buildOutput: O.() -> Unit) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleObject(name, GraphQLInput(this.parameters[1].name!!, input), buildOutput)
    }

    @JvmName("asObject_KFunction2____I__List_O__")
    suspend inline fun <I, reified O : Any> KFunction2<*, I, List<O>>.asObject(input: I, buildOutput: O.() -> Unit) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleObject(name, GraphQLInput(this.parameters[1].name!!, input), buildOutput)
    }

    @JvmName("asObject_KFunction2____I__CompletableFuture_O__")
    suspend inline fun <I, reified O : Any> KFunction2<*, I, CompletableFuture<O>>.asObjectLazy(
        input: I,
        buildOutput: O.() -> Unit,
    ) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleObject(name, GraphQLInput(this.parameters[1].name!!, input), buildOutput)
    }

    @JvmName("asObject_KFunction2____I__CompletableFuture_O___")
    suspend inline fun <I, reified O : Any> KFunction2<*, I, CompletableFuture<O?>>.asObjectLazy(
        input: I,
        buildOutput: O.() -> Unit,
    ) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleObject(name, GraphQLInput(this.parameters[1].name!!, input), buildOutput)
    }

    @JvmName("asObject_KFunction2____I__CompletableFuture_List_O__")
    suspend inline fun <I, reified O : Any> KFunction2<*, I, CompletableFuture<List<O>>>.asObjectLazy(
        input: I,
        buildOutput: O.() -> Unit,
    ) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleObject(name, GraphQLInput(this.parameters[1].name!!, input), buildOutput)
    }

    @JvmName("asObject_KFunction2____I__CompletableFuture_List_O___")
    suspend inline fun <I, reified O : Any> KFunction2<*, I, CompletableFuture<List<O?>>>.asObjectLazy(
        input: I,
        buildOutput: O.() -> Unit,
    ) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleObject(name, GraphQLInput(this.parameters[1].name!!, input), buildOutput)
    }

    @JvmName("asObject_KFunction3_I________O__")
    suspend inline fun <I, reified O : Any> KFunction3<I, *, *, O?>.asObject(input: I, buildOutput: O.() -> Unit) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleObject(name, GraphQLInput(this.parameters[0].name!!, input), buildOutput)
    }

    @JvmName("asObject_KFunction3_I________List_O___")
    suspend inline fun <I, reified O : Any> KFunction3<I, *, *, List<O?>>.asObject(
        input: I,
        buildOutput: O.() -> Unit,
    ) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleObject(name, GraphQLInput(this.parameters[0].name!!, input), buildOutput)
    }

    @JvmName("asObject_KFunction3_I________List_O__")
    suspend inline fun <I, reified O : Any> KFunction3<I, *, *, List<O>>.asObject(
        input: I,
        buildOutput: O.() -> Unit,
    ) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleObject(name, GraphQLInput(this.parameters[0].name!!, input), buildOutput)
    }

    @JvmName("asObject_KFunction3_I________CompletableFuture_O__")
    suspend inline fun <I, reified O : Any> KFunction3<I, *, *, CompletableFuture<O>>.asObjectLazy(
        input: I,
        buildOutput: O.() -> Unit,
    ) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleObject(name, GraphQLInput(this.parameters[0].name!!, input), buildOutput)
    }

    @JvmName("asObject_KFunction3_I________CompletableFuture_O___")
    suspend inline fun <I, reified O : Any> KFunction3<I, *, *, CompletableFuture<O?>>.asObjectLazy(
        input: I,
        buildOutput: O.() -> Unit,
    ) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleObject(name, GraphQLInput(this.parameters[0].name!!, input), buildOutput)
    }

    @JvmName("asObject_KFunction3_I________CompletableFuture_List_O__")
    suspend inline fun <I, reified O : Any> KFunction3<I, *, *, CompletableFuture<List<O>>>.asObjectLazy(
        input: I,
        buildOutput: O.() -> Unit,
    ) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleObject(name, GraphQLInput(this.parameters[0].name!!, input), buildOutput)
    }

    @JvmName("asObject_KFunction3_I________CompletableFuture_List_O___")
    suspend inline fun <I, reified O : Any> KFunction3<I, *, *, CompletableFuture<List<O?>>>.asObjectLazy(
        input: I,
        buildOutput: O.() -> Unit,
    ) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleObject(name, GraphQLInput(this.parameters[0].name!!, input), buildOutput)
    }

    @JvmName("asObject_KSuspendFunction1_I_O__")
    suspend inline fun <I, reified O : Any> KSuspendFunction1<I, O?>.asObject(input: I, buildOutput: O.() -> Unit) {
        require(O::class in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleObject(name, GraphQLInput(this.parameters[0].name!!, input), buildOutput)
    }

    @JvmName("asObject_KSuspendFunction1_I_List_O___")
    suspend inline fun <I, reified O : Any> KSuspendFunction1<I, List<O?>>.asObject(
        input: I,
        buildOutput: O.() -> Unit,
    ) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleObject(name, GraphQLInput(this.parameters[0].name!!, input), buildOutput)
    }

    @JvmName("asObject_KSuspendFunction1_I_List_O__")
    suspend inline fun <I, reified O : Any> KSuspendFunction1<I, List<O>>.asObjects(
        input: I,
        buildOutput: O.() -> Unit,
    ) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleObject(name, GraphQLInput(this.parameters[0].name!!, input), buildOutput)
    }

    @JvmName("asObject_KSuspendFunction2_I_____O__")
    suspend inline fun <I, reified O : Any> KSuspendFunction2<I, *, O?>.asObject(input: I, buildOutput: O.() -> Unit) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleObject(name, GraphQLInput(this.parameters[0].name!!, input), buildOutput)
    }

    @JvmName("asObject_KSuspendFunction2_I_____List_O___")
    suspend inline fun <I, reified O : Any> KSuspendFunction2<I, *, List<O?>>.asObjects(
        input: I,
        buildOutput: O.() -> Unit,
    ) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleObject(name, GraphQLInput(this.parameters[0].name!!, input), buildOutput)
    }

    @JvmName("asObject_KSuspendFunction2_I_____CompletableFuture_O__")
    suspend inline fun <I, reified O : Any> KSuspendFunction2<I, *, CompletableFuture<O>>.asObjectLazy(
        input: I,
        buildOutput: O.() -> Unit,
    ) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleObject(name, GraphQLInput(this.parameters[0].name!!, input), buildOutput)
    }

    @JvmName("asObject_KSuspendFunction2_I_____List_O__")
    suspend inline fun <I, reified O : Any> KSuspendFunction2<I, *, List<O>>.asObjects(
        input: I,
        buildOutput: O.() -> Unit,
    ) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleObject(name, GraphQLInput(this.parameters[0].name!!, input), buildOutput)
    }

    @JvmName("asObject_KSuspendFunction2____I__O__")
    suspend inline fun <I, reified O : Any> KSuspendFunction2<*, I, O?>.asObject(input: I, buildOutput: O.() -> Unit) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleObject(name, GraphQLInput(this.parameters[1].name!!, input), buildOutput)
    }

    @JvmName("asObject_KSuspendFunction2____I__List_O___")
    suspend inline fun <I, reified O : Any> KSuspendFunction2<*, I, List<O?>>.asObjects(
        input: I,
        buildOutput: O.() -> Unit,
    ) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleObject(name, GraphQLInput(this.parameters[1].name!!, input), buildOutput)
    }

    @JvmName("asObject_KSuspendFunction2____I__List_O__")
    suspend inline fun <I, reified O : Any> KSuspendFunction2<*, I, List<O>>.asObjects(
        input: I,
        buildOutput: O.() -> Unit,
    ) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleObject(name, GraphQLInput(this.parameters[1].name!!, input), buildOutput)
    }

    @JvmName("asObject_KSuspendFunction3_I________O__")
    suspend inline fun <I, reified O : Any> KSuspendFunction3<I, *, *, O?>.asObject(
        input: I,
        buildOutput: O.() -> Unit,
    ) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleObject(name, GraphQLInput(this.parameters[0].name!!, input), buildOutput)
    }

    @JvmName("asObject_KSuspendFunction3_I________List_O___")
    suspend inline fun <I, reified O : Any> KSuspendFunction3<I, *, *, List<O?>>.asObjects(
        input: I,
        buildOutput: O.() -> Unit,
    ) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleObject(name, GraphQLInput(this.parameters[0].name!!, input), buildOutput)
    }

    @JvmName("asObject_KSuspendFunction3_I________List_O__")
    suspend inline fun <I, reified O : Any> KSuspendFunction3<I, *, *, List<O>>.asObjects(
        input: I,
        buildOutput: O.() -> Unit,
    ) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleObject(name, GraphQLInput(this.parameters[0].name!!, input), buildOutput)
    }

    @JvmName("asObject_KFunction4_I_J_____O__")
    suspend inline fun <I, J, reified O : Any> KFunction4<I, J, *, *, O?>.asObject(
        input: I,
        input2: J,
        buildOutput: O.() -> Unit,
    ) {
        require(O::class !in scalarType && !O::class.java.isEnum) { "${O::class.simpleName} seems scalar type" }
        handleObject(
            name,
            GraphQLInput(this.parameters[0].name!!, input),
            GraphQLInput(this.parameters[1].name!!, input2),
            buildOutput = buildOutput
        )
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    suspend inline fun <T, reified O : T> T.asInlineFragment(buildOutput: O.() -> Unit) {
        val inlineFragmentName = O::class.findAnnotation<GraphQLName>()?.value ?: O::class.simpleName
        addWithIndent("... on $inlineFragmentName {")
        currentIndent += INDENT_SIZE
        instantiate<O>().buildOutput()
        currentIndent -= INDENT_SIZE
        addWithIndent("}")
    }

    suspend inline fun <T, reified O : T> T.asInlineFragmentWithTypeName(buildOutput: O.() -> Unit) {
        val inlineFragmentName = O::class.findAnnotation<GraphQLName>()?.value ?: O::class.simpleName
        addWithIndent("... on $inlineFragmentName {")
        currentIndent += INDENT_SIZE
        addWithIndent("__typename")
        instantiate<O>().buildOutput()
        currentIndent -= INDENT_SIZE
        addWithIndent("}")
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // common method
    fun <I> formatInput(input: I): String = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(input)
        .replace("\"([a-zA-Z_][a-zA-Z0-9_]*)\"\\s*:".toRegex(), "$1:")
        .let { json ->
            // Only remove quotes for actual Enum values
            if (input != null && input::class.java.isEnum) {
                json.replace(": \"([A-Z_]+)\"".toRegex(), ": $1")  // Enumのクォート削除（大文字のみマッチ）
            } else {
                // For complex objects, we need to use reflection to identify enum fields
                processComplexObject(input, json, mutableSetOf())
            }
        }
        .split("\n")
        .mapIndexed { index, line -> if (index == 0) line else (" ".repeat(currentIndent) + line) }
        .joinToString("\n")

    /**
     * Process a complex object to identify enum fields using reflection
     * and remove quotes from enum values
     */
    private fun <I> processComplexObject(input: I, json: String, processedObjects: MutableSet<Any> = mutableSetOf()): String {
        if (input == null) return json

        // Prevent infinite recursion by tracking processed objects
        if (true && !processedObjects.add(input)) {
            return json // Skip already processed objects
        }

        // Get all fields of the input object
        val fields = input::class.java.declaredFields

        var processedJson = json

        // Check each field to see if it's an enum type
        for (field in fields) {
            // Try to make the field accessible, but catch InaccessibleObjectException
            // This can happen with Java 9+ module system restrictions
            try {
                field.isAccessible = true
            } catch (e: Exception) {
                // If we can't make the field accessible, skip it
                continue
            }

            // Get the field value
            val fieldValue = try {
                field.get(input)
            } catch (e: Exception) {
                null
            }

            // If the field is an enum type or its value is an enum
            if (field.type.isEnum || (fieldValue != null && fieldValue::class.java.isEnum)) {
                // Remove quotes from the enum value in the JSON
                val fieldName = field.name
                processedJson = processedJson.replace("$fieldName:\\s*\"([A-Z_]+)\"".toRegex(), "$fieldName: $1")
            }
            // If the field is a collection, process each item in the collection
            else if (fieldValue is Collection<*>) {
                // For collections, we need to check each item
                fieldValue.forEachIndexed { index, item ->
                    if (item != null && item::class.java.isEnum) {
                        // For enum items in a collection, we need to match the pattern that includes the index
                        processedJson = processedJson.replace("\"([A-Z_]+)\"".toRegex()) { matchResult ->
                            val enumValue = matchResult.groupValues[1]
                            // Check if this enum value matches the item's name
                            if (enumValue == (item as Enum<*>).name) {
                                enumValue // Return without quotes
                            } else {
                                matchResult.value // Keep the quotes
                            }
                        }
                    } else if (item != null && !isPrimitiveOrString(item::class.java)) {
                        // Recursively process complex objects in collections
                        processedJson = processComplexObject(item, processedJson, processedObjects)
                    }
                }
            }
            // If the field is a complex object (not a primitive or string), process it recursively
            else if (fieldValue != null && !isPrimitiveOrString(fieldValue::class.java)) {
                processedJson = processComplexObject(fieldValue, processedJson, processedObjects)
            }
        }

        return processedJson
    }

    /**
     * Check if a class is a primitive type or String
     */
    private fun isPrimitiveOrString(clazz: Class<*>): Boolean {
        return clazz.isPrimitive ||
            clazz == String::class.java ||
            clazz == Boolean::class.java ||
            clazz == Char::class.java ||
            clazz == Byte::class.java ||
            clazz == Short::class.java ||
            clazz == Int::class.java ||
            clazz == Long::class.java ||
            clazz == Float::class.java ||
            clazz == Double::class.java ||
            clazz == Void::class.java
    }

    fun handleField(name: String) {
        addWithIndent(name)
    }

    fun <I> handleField(name: String, input: GraphQLInput<I>) {
        val formattedInput = formatInput(input.value)
        addWithIndent("$name(${input.name}: $formattedInput)")
    }

    fun handleQuery(prefix: String, name: String) {
        addWithIndent("$prefix {")
        currentIndent += INDENT_SIZE
        handleField(name)
        currentIndent -= INDENT_SIZE
        addWithIndent("}")
    }

    suspend inline fun <reified O> handleQuery(prefix: String, name: String, buildOutput: O.() -> Unit) {
        addWithIndent("$prefix {")
        currentIndent += INDENT_SIZE
        handleObject<O>(name, buildOutput)
        currentIndent -= INDENT_SIZE
        addWithIndent("}")
    }

    fun <I> handleQuery(
        prefix: String,
        name: String,
        input: GraphQLInput<I>,
    ) {
        addWithIndent("$prefix {")
        currentIndent += INDENT_SIZE
        handleField(name, input)
        currentIndent -= INDENT_SIZE
        addWithIndent("}")
    }

    suspend inline fun <I, reified O> handleQuery(
        prefix: String,
        name: String,
        input: GraphQLInput<I>,
        buildOutput: O.() -> Unit,
    ) {
        addWithIndent("$prefix {")
        currentIndent += INDENT_SIZE
        handleObject(name, input, buildOutput)
        currentIndent -= INDENT_SIZE
        addWithIndent("}")
    }

    suspend inline fun <reified O> handleObject(name: String, buildOutput: O.() -> Unit) {
        addWithIndent("$name {")
        currentIndent += INDENT_SIZE
        instantiate<O>().buildOutput()
        currentIndent -= INDENT_SIZE
        addWithIndent("}")
    }

    suspend inline fun <I, reified O> handleObject(name: String, input: GraphQLInput<I>, buildOutput: O.() -> Unit) {
        val formattedInput = formatInput(input.value)
        addWithIndent("$name(${input.name}: $formattedInput) {")
        currentIndent += INDENT_SIZE
        instantiate<O>().buildOutput()
        currentIndent -= INDENT_SIZE
        addWithIndent("}")
    }

    suspend inline fun <I, reified O> handleObject(
        name: String,
        vararg inputs: GraphQLInput<I>,
        buildOutput: O.() -> Unit,
    ) {
        val formattedInputs = inputs.map { formatInput(it.value) }
        addWithIndent("$name(${inputs.joinToString(", ") { it.name + formattedInputs[inputs.indexOf(it)] }}) {")
        currentIndent += INDENT_SIZE
        instantiate<O>().buildOutput()
        currentIndent -= INDENT_SIZE
        addWithIndent("}")
    }

    suspend inline fun <reified O> instantiate(): O = if (O::class.isSealed) {
        ObjenesisStd().newInstance(O::class.sealedSubclasses.first().java)
    } else {
        ObjenesisStd().newInstance(O::class.java)
    }
}

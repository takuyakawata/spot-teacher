package com.spotteacher.admin.shared.graphql

import com.expediagroup.graphql.generator.hooks.SchemaGeneratorHooks
import com.spotteacher.graphql.NonEmptyString
import graphql.GraphQLContext
import graphql.execution.CoercedVariables
import graphql.language.IntValue
import graphql.language.StringValue
import graphql.language.Value
import graphql.scalars.datetime.DateScalar
import graphql.scalars.datetime.DateTimeScalar
import graphql.scalars.datetime.TimeScalar
import graphql.schema.Coercing
import graphql.schema.CoercingParseValueException
import graphql.schema.CoercingSerializeException
import graphql.schema.GraphQLScalarType
import graphql.schema.GraphQLType
import org.springframework.stereotype.Component
import java.math.BigInteger
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KType

@Component
class CustomSchemaGeneratorHooks : SchemaGeneratorHooks {
    override fun willGenerateGraphQLType(type: KType): GraphQLType? = when (type.classifier as? KClass<*>) {
        Unit::class -> unitType
        LocalDate::class -> DateScalar.INSTANCE
        LocalDateTime::class -> localDatetimeType
        LocalTime::class -> TimeScalar.INSTANCE
        ZonedDateTime::class -> DateTimeScalar.INSTANCE
        NonEmptyString::class -> nonEmptyStringType
        UInt::class -> uIntType
        else -> null
    }

    private val unitType = GraphQLScalarType.newScalar()
        .name("Unit")
        .description("meaningless value. Necessary just for graphql limitation. Only used for output.")
        .coercing(object : Coercing<Unit, String> {
            override fun serialize(dataFetcherResult: Any, graphQLContext: GraphQLContext, locale: Locale): String =
                "Unit"
        })
        .build()

    private val nonEmptyStringType = GraphQLScalarType.newScalar()
        .name("NonEmptyString")
        .description("String that is not empty")
        .coercing(object : Coercing<NonEmptyString, String> {
            override fun serialize(dataFetcherResult: Any, graphQLContext: GraphQLContext, locale: Locale): String {
                return (dataFetcherResult as? NonEmptyString)?.value
                    ?: throw CoercingSerializeException("Expected NonEmptyString object")
            }

            override fun parseValue(input: Any, graphQLContext: GraphQLContext, locale: Locale): NonEmptyString? {
                val result = input.toString()
                return if (result.isBlank()) null else NonEmptyString(result)
            }

            override fun parseLiteral(
                input: Value<*>,
                variables: CoercedVariables,
                graphQLContext: GraphQLContext,
                locale: Locale,
            ): NonEmptyString? {
                val result = (input as? StringValue)?.value
                return if (result.isNullOrBlank()) null else NonEmptyString(result)
            }
        })
        .build()

    private val localDatetimeType = GraphQLScalarType.newScalar()
        .name("LocalDateTime")
        .description("LocalDateTime")
        .coercing(object : Coercing<LocalDateTime?, String> {
            override fun serialize(dataFetcherResult: Any, graphQLContext: GraphQLContext, locale: Locale): String {
                return (dataFetcherResult as? LocalDateTime)?.toString()
                    ?: throw CoercingSerializeException("Expected a LocalDateTime object.")
            }

            override fun parseValue(input: Any, graphQLContext: GraphQLContext, locale: Locale): LocalDateTime? {
                return try {
                    if (input is String) {
                        LocalDateTime.parse(input, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                    } else {
                        throw CoercingParseValueException("Expected a String")
                    }
                } catch (e: DateTimeParseException) {
                    throw CoercingParseValueException("Could not parse LocalDateTime", e)
                }
            }

            override fun parseLiteral(
                input: Value<*>,
                variables: CoercedVariables,
                graphQLContext: GraphQLContext,
                locale: Locale,
            ): LocalDateTime? {
                return if (input is StringValue) {
                    try {
                        LocalDateTime.parse(input.value, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                    } catch (e: DateTimeParseException) {
                        throw CoercingParseValueException("Could not parse LocalDateTime", e)
                    }
                } else {
                    throw CoercingParseValueException("Expected a StringValue")
                }
            }
        })
        .build()

    private val uIntType = GraphQLScalarType.newScalar()
        .name("UInt")
        .description("UInt positive integer")
        .coercing(object : Coercing<UInt, Int> {

            override fun parseValue(input: Any, graphQLContext: GraphQLContext, locale: Locale): UInt {
                return (input as? Int)?.toUInt()?.takeIf { it >= 0u }
                    ?: throw CoercingSerializeException("The value must be a positive integer")
            }

            override fun parseLiteral(
                input: Value<*>,
                variables: CoercedVariables,
                graphQLContext: GraphQLContext,
                locale: Locale,
            ): UInt {
                return if (input is IntValue && input.value >= BigInteger.ZERO) {
                    input.value.toInt().toUInt()
                } else {
                    throw CoercingSerializeException("The value must be a positive integer")
                }
            }
        })
        .build()
}

package com.spotteacher.admin.feature.school.handler

import com.expediagroup.graphql.generator.annotations.GraphQLName
import com.spotteacher.admin.feature.school.domain.SchoolCategory
import com.spotteacher.admin.feature.school.domain.SchoolId
import com.spotteacher.graphql.toID

private const val SCHOOL_TYPE = "School"

@GraphQLName(SCHOOL_TYPE)
data class SchoolType(
    val id: String,
    val name: String,
    val schoolCategory: SchoolCategory,
    val city: String,
    val postalCode: String,
    val prefecture: String,
    val streetAddress: String,
    val buildingName: String?,
    val url: String?,
    val phoneNumber: String?,
)

fun SchoolId.toGraphQLID() = toID(SCHOOL_TYPE)
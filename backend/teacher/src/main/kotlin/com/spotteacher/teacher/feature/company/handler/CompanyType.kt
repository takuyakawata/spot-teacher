package com.spotteacher.teacher.feature.company.handler

import com.expediagroup.graphql.generator.annotations.GraphQLName
import com.expediagroup.graphql.generator.scalars.ID

import com.spotteacher.graphql.toID
import com.spotteacher.teacher.feature.company.domain.CompanyId
import java.time.LocalDateTime

private const val COMPANY_TYPE = "Company"

@GraphQLName(COMPANY_TYPE)
data class CompanyType(
    val id: ID,
    val name: String,
    val postalCode: String,
    val prefecture: String,
    val city: String,
    val streetAddress: String,
    val buildingName: String?,
    val phoneNumber: String?,
    val url: String?,
    val createdAt: LocalDateTime
)

fun CompanyId.toGraphQLID() = this.toID(COMPANY_TYPE)

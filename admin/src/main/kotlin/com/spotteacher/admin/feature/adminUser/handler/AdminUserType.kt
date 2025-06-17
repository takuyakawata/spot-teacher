package com.spotteacher.admin.feature.adminUser.handler

import com.expediagroup.graphql.generator.annotations.GraphQLName
import com.expediagroup.graphql.generator.scalars.ID

private const val ADMIN_USER_TYPE = "AdminUser"

@GraphQLName(ADMIN_USER_TYPE)
data class AdminUserType(
    val id : ID,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val isActive: Boolean,
)

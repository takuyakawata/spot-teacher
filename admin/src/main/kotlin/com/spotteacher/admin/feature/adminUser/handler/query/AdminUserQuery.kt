package com.spotteacher.admin.feature.adminUser.handler.query

import com.expediagroup.graphql.server.operations.Query
import com.spotteacher.admin.feature.adminUser.handler.AdminUserType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component

@Component
class AdminUserQuery : Query {
    @PreAuthorize("isAuthenticated()")
    suspend fun adminUsers(): List<AdminUserType> {
        return emptyList()
    }
}

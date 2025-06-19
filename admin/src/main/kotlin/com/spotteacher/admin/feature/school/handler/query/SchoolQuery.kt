package com.spotteacher.admin.feature.school.handler.query

import com.expediagroup.graphql.server.operations.Query
import com.spotteacher.admin.feature.school.handler.SchoolType
import org.springframework.stereotype.Component

@Component
class SchoolQuery : Query {
    suspend fun schools(): List<SchoolType> = emptyList()
}

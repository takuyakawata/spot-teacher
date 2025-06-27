package com.spotteacher.admin.feature.lessonTag.handler.query

import com.expediagroup.graphql.server.operations.Query
import com.spotteacher.admin.feature.lessonTag.domain.EducationName
import com.spotteacher.admin.feature.lessonTag.handler.EducationType
import com.spotteacher.admin.feature.lessonTag.handler.toEducationType
import com.spotteacher.admin.feature.lessonTag.usecase.FindEducationsFilterByNameUseCase
import com.spotteacher.admin.feature.lessonTag.usecase.GetAllEducationsUseCase
import com.spotteacher.graphql.NonEmptyString
import org.springframework.stereotype.Component

@Component
class EducationQuery(
    private val findEducationsFilterByNameUseCase: FindEducationsFilterByNameUseCase,
    private val getAllEducationsUseCase: GetAllEducationsUseCase
) : Query {
    suspend fun educationFindByName(name: NonEmptyString): EducationType? =
        findEducationsFilterByNameUseCase.call(EducationName(name.value))?.toEducationType()

    suspend fun educationGetAll(): List<EducationType> = getAllEducationsUseCase.call().map { it.toEducationType() }
}

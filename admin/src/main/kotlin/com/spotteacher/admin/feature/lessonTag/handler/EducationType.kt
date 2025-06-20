package com.spotteacher.admin.feature.lessonTag.handler

import com.expediagroup.graphql.generator.annotations.GraphQLType
import com.expediagroup.graphql.generator.scalars.ID
import com.spotteacher.admin.feature.lessonTag.domain.Education
import com.spotteacher.admin.feature.lessonTag.domain.EducationId
import com.spotteacher.admin.feature.lessonTag.domain.Grade
import com.spotteacher.admin.feature.lessonTag.domain.Subject
import com.spotteacher.graphql.toID

const val EDUCATION_TYPE = "education"

@GraphQLType(EDUCATION_TYPE)
data class EducationType(
    val id: ID,
    val name: String,
    val isActive: Boolean,
    val displayOrder: Int,
){
//    fun educations(): List<Education> = FindEducationsUseCase().findEducations()
    fun subjects(): List<Subject> = Subject.entries
    fun grades(): List<Grade> = Grade.entries
}

fun Education.toEducationType() = EducationType(
    id = this.id.toGraphQLID(),
    name = this.name.value,
    isActive = this.isActive,
    displayOrder = this.displayOrder,
)

fun EducationId.toGraphQLID() = this.toID(EDUCATION_TYPE)

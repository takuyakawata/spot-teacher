package com.spotteacher.admin.feature.teacher.handler

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.expediagroup.graphql.generator.annotations.GraphQLName
import com.expediagroup.graphql.generator.scalars.ID
import com.spotteacher.admin.feature.school.domain.SchoolId
import com.spotteacher.admin.feature.school.handler.SchoolType
import com.spotteacher.admin.feature.teacher.domain.TeacherId
import com.spotteacher.graphql.toID

private const val TEACHER_TYPE = "Teacher"

@GraphQLName(TEACHER_TYPE)
data class TeacherType(
    val id: ID,
    @GraphQLIgnore
    val schoolId: SchoolId,
    val firstName: String,
    val lastName: String,
    val email: String,
){
    fun school(): SchoolType? = null
}

fun TeacherId.toGraphQLID() = toID(TEACHER_TYPE)
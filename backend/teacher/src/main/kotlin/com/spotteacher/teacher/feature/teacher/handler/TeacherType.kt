package com.spotteacher.teacher.feature.teacher.handler

import com.expediagroup.graphql.generator.annotations.GraphQLName
import com.expediagroup.graphql.generator.scalars.ID

private const val TEACHER_TYPE = "Teacher"

@GraphQLName(TEACHER_TYPE)
data class TeacherType(
    val id: ID,
    val userId: Long,
    val schoolId: Long
)

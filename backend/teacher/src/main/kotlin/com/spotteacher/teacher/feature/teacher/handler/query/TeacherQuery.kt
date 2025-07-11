package com.spotteacher.teacher.feature.teacher.handler.query

import com.expediagroup.graphql.generator.scalars.ID
import com.expediagroup.graphql.server.operations.Query
import com.spotteacher.teacher.feature.teacher.domain.TeacherId
import com.spotteacher.teacher.feature.teacher.handler.TeacherType
import com.spotteacher.teacher.feature.teacher.usecase.FindTeacherUseCase
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component

@Component
class TeacherQuery(
    private val findTeacherUseCase: FindTeacherUseCase
) : Query {

    @PreAuthorize("isAuthenticated()")
    suspend fun teachers(): List<TeacherType> {
        val teachers = findTeacherUseCase.getAll()
        return teachers.map { teacher ->
            TeacherType(
                id = ID(teacher.id.value.toString()),
                schoolId = teacher.schoolId
            )
        }
    }

    @PreAuthorize("isAuthenticated()")
    suspend fun teacher(id: ID): TeacherType? {
        val teacher = findTeacherUseCase.findById(TeacherId(id.value.toLong()))
        return teacher?.let {
            TeacherType(
                id = ID(it.id.value.toString()),
                schoolId = it.schoolId
            )
        }
    }

    @PreAuthorize("isAuthenticated()")
    suspend fun teacherByUser(userId: Long): TeacherType? {
        val teacher = findTeacherUseCase.findByUserId(userId)
        return teacher?.let {
            TeacherType(
                id = ID(it.id.value.toString()),
                schoolId = it.schoolId
            )
        }
    }

    @PreAuthorize("isAuthenticated()")
    suspend fun teachersBySchool(schoolId: Long): List<TeacherType> {
        val teachers = findTeacherUseCase.findBySchoolId(schoolId)
        return teachers.map { teacher ->
            TeacherType(
                id = ID(teacher.id.value.toString()),
                schoolId = teacher.schoolId
            )
        }
    }
}

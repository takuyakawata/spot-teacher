package com.spotteacher.teacher.feature.teacher.domain

import com.spotteacher.util.Identity

sealed interface Teacher {
    val id: TeacherId
    val userId: Long
    val schoolId: Long
}

data class ActiveTeacher(
    override val id: TeacherId,
    override val userId: Long,
    override val schoolId: Long
) : Teacher {
    companion object {
        fun create(
            userId: Long,
            schoolId: Long
        ) = ActiveTeacher(
            id = TeacherId(0),
            userId = userId,
            schoolId = schoolId
        )
    }
}

fun ActiveTeacher.toInActiveTeacher() = InActiveTeacher(
    id = this.id,
    userId = this.userId,
    schoolId = this.schoolId
)

/**
 * Represents an inactive teacher (使用停止中の先生).
 *
 * @property id The unique identifier for the teacher.
 * @property userId The user ID reference.
 * @property schoolId The school ID reference.
 */
data class InActiveTeacher(
    override val id: TeacherId,
    override val userId: Long,
    override val schoolId: Long
) : Teacher {
    companion object {
        fun create(
            userId: Long,
            schoolId: Long
        ) = InActiveTeacher(
            id = TeacherId(0),
            userId = userId,
            schoolId = schoolId
        )
    }
}

fun InActiveTeacher.toActiveTeacher() = ActiveTeacher(
    id = this.id,
    userId = this.userId,
    schoolId = this.schoolId
)

// Entity のIDは
class TeacherId(override val value: Long) : Identity<Long>(value)

data class TeacherError(
    val code: TeacherErrorCode,
    val message: String
)

enum class TeacherErrorCode {
    TEACHER_NOT_FOUND,
    TEACHER_ALREADY_EXISTS,
}

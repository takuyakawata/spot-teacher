package com.spotteacher.teacher.feature.teacher.domain

import com.spotteacher.domain.EmailAddress
import com.spotteacher.teacher.feature.school.domain.SchoolId
import com.spotteacher.util.Identity

// todo 削除の動線を考えるとき、定義の仕方を変更する
data class Teacher(
    val id: TeacherId,
    val schoolId: SchoolId,
    val firstName: TeacherName,
    val lastName: TeacherName,
    val email: EmailAddress,
) {
    companion object {
        fun create(
            schoolId: SchoolId,
            firstName: TeacherName,
            lastName: TeacherName,
            email: EmailAddress,
        ) = Teacher(
            id = TeacherId(0),
            schoolId = schoolId,
            firstName = firstName,
            lastName = lastName,
            email = email,
        )
    }
}

class TeacherId(override val value: Long) : Identity<Long>(value)

@JvmInline
value class TeacherName(val value: String) {
    companion object {
        const val MAX_LENGTH = 200
    }
    init {
       require(value.length <= MAX_LENGTH) { "Name must be less than $MAX_LENGTH characters" }
    }
}

data class TeacherError(
    val code: TeacherErrorCode,
    val message: String
)

enum class TeacherErrorCode {
    TEACHER_NOT_FOUND,
    TEACHER_ALREADY_EXISTS,
}

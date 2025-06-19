package com.spotteacher.admin.feature.school.domain

import com.spotteacher.domain.Address
import com.spotteacher.domain.PhoneNumber
import com.spotteacher.util.Identity

data class School(
    val id: SchoolId,
    val schoolCode: SchoolCode,
    val name: SchoolName,
    val schoolCategory: SchoolCategory,
    val address: Address,
    val phoneNumber: PhoneNumber,
    val url: String?
)

class SchoolId(override val value: Long) : Identity<Long>(value)

@JvmInline
value class SchoolCode(val value: String)

@JvmInline
value class SchoolName(val value: String)

enum class SchoolCategory {
    ELEMENTARY, JUNIOR_HIGH, HIGH
}

data class SchoolError(
    val message: String,
    val code: SchoolErrorCode
)

enum class SchoolErrorCode {
    SCHOOL_NOT_FOUND,
    SCHOOL_ALREADY_EXISTS,
}

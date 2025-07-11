package com.spotteacher.teacher.feature.school.domain

import com.spotteacher.domain.Address
import com.spotteacher.domain.PhoneNumber
import com.spotteacher.util.Identity

data class School(
    val id: SchoolId,
//    val schoolCode: SchoolCode?,//todo empl yet
    val name: SchoolName,
    val schoolCategory: SchoolCategory,
    val address: Address,
    val phoneNumber: PhoneNumber,
    val url: String?
) {
    companion object {
        fun create(
            name: SchoolName,
            schoolCategory: SchoolCategory,
            address: Address,
            phoneNumber: PhoneNumber,
            url: String?
        ) = School(
            id = SchoolId(0),
            name = name,
            schoolCategory = schoolCategory,
            address = address,
            phoneNumber = phoneNumber,
            url = url,
        )
    }
}

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

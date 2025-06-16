package com.spotteacher.admin.feature.company.domain

import com.spotteacher.domain.Address
import com.spotteacher.domain.PhoneNumber
import com.spotteacher.util.Identity
import java.net.URI
import java.time.LocalDateTime

data class Company(
    val id: CompanyId,
    val name: CompanyName,
    val address : Address,
    val phoneNumber : PhoneNumber,
    val url: URI?,
    val createdAt : LocalDateTime
){
    companion object{
        fun create(
            name: CompanyName,
            address: Address,
            phoneNumber: PhoneNumber,
            url: URI?,
        ) = Company(
            id = CompanyId(0),
            name = name,
            address = address,
            phoneNumber = phoneNumber,
            url = url,
            createdAt = LocalDateTime.now()
        )
    }
}

class CompanyId(override val value:Long): Identity<Long>(value)

@JvmInline
value class CompanyName(val value: String)

data class CompanyError(
    val message: String,
    val code: CompanyErrorCode
)

enum class CompanyErrorCode {
    COMPANY_NOT_FOUND,
    COMPANY_ALREADY_EXISTS,
}

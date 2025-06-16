package com.spotteacher.admin.feature.company.handler.query

import arrow.core.Either
import com.expediagroup.graphql.generator.scalars.ID
import com.expediagroup.graphql.server.operations.Query
import com.spotteacher.admin.feature.company.domain.Company
import com.spotteacher.admin.feature.company.domain.CompanyErrorCode
import com.spotteacher.admin.feature.company.domain.CompanyId
import com.spotteacher.admin.feature.company.handler.CompanyType
import com.spotteacher.admin.feature.company.handler.toGraphQLID
import com.spotteacher.admin.feature.company.usecase.FindCompanyUseCase
import org.springframework.stereotype.Component

sealed interface CompanyQueryOutput
data class CompanyQuerySuccess(val company: CompanyType): CompanyQueryOutput
data class CompanyQueryError(
    val error: CompanyErrorCode,
    val message: String? = null
): CompanyQueryOutput

@Component
class CompanyQuery(
    private val findCompanyUseCase: FindCompanyUseCase
    //private val findCompaniesUseCase: FindCompaniesUseCase
): Query {
    suspend fun company(id: ID): CompanyQueryOutput{
        val result = findCompanyUseCase.call(CompanyId(id.value.toLong())).result

        return result.fold(
            ifLeft = { error -> 
                CompanyQueryError(
                    error = error.code,
                    message = error.message
                )
            },
            ifRight = { company -> 
                CompanyQuerySuccess(
                    company = CompanyType(
                        id = company.id.toGraphQLID(),
                        name = company.name.value,
                        postalCode = company.address.postCode.value,
                        prefecture = company.address.prefecture.name,
                        city = company.address.city.value,
                        streetAddress = company.address.streetAddress.value,
                        buildingName = company.address.buildingName?.value,
                        phoneNumber = company.phoneNumber.value,
                        url = company.url?.toString(),
                        createdAt = company.createdAt
                    )
                )
            }
        )
    }

    fun companies():List<Company>{
        TODO("Not yet implemented")
    }
}

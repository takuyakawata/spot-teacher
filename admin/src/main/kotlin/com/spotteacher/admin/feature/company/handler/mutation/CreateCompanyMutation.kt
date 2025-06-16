package com.spotteacher.admin.feature.company.handler.mutation

import com.expediagroup.graphql.server.operations.Mutation
import com.spotteacher.admin.feature.company.domain.CompanyErrorCode
import com.spotteacher.admin.feature.company.domain.CompanyName
import com.spotteacher.admin.feature.company.handler.CompanyType
import com.spotteacher.admin.feature.company.handler.toGraphQLID
import com.spotteacher.admin.feature.company.usecase.CreateCompanyUseCase
import com.spotteacher.admin.feature.company.usecase.CreateCompanyUseCaseInput
import com.spotteacher.domain.Address
import com.spotteacher.domain.BuildingName
import com.spotteacher.domain.City
import com.spotteacher.domain.PhoneNumber
import com.spotteacher.domain.PostCode
import com.spotteacher.domain.Prefecture
import com.spotteacher.domain.StreetAddress
import org.springframework.stereotype.Component
import java.net.URI

data class CreateCompanyMutationInput(
    val name: String,
    val postalCode: String,
    val prefecture: String,
    val city: String,
    val streetAddress: String,
    val buildingName: String?,
     val phoneNumber: String,
     val url: String?

)

sealed interface CreateCompanyMutationOutput {
    data class CreateCompanyMutationSuccess(val company: CompanyType) : CreateCompanyMutationOutput
    data class CreateCompanyMutationError(
    val message: String,
    val code: CompanyErrorCode
    ) : CreateCompanyMutationOutput
}

@Component
class CreateCompanyMutation(
    private val usecase: CreateCompanyUseCase
) : Mutation {
    suspend fun createCompany(input: CreateCompanyMutationInput): CreateCompanyMutationOutput {
        // Create address
        val address = Address(
            postCode = PostCode(input.postalCode),
            prefecture = Prefecture.valueOf(input.prefecture),
            city = City(input.city),
            streetAddress = StreetAddress(input.streetAddress),
            buildingName = input.buildingName?.let { BuildingName(it) }
        )

        // Call use case
        val result = usecase.call(
            CreateCompanyUseCaseInput(
                name = CompanyName(input.name),
                address = address,
                phoneNumber = PhoneNumber(input.phoneNumber),
                url = input.url?.let { URI(it) }
            )
        ).result

        return result.fold(
            ifLeft = { error -> CreateCompanyMutationOutput.CreateCompanyMutationError(
                message = error.message,
                code = error.code
            ) },
            ifRight = { company -> CreateCompanyMutationOutput.CreateCompanyMutationSuccess(
            CompanyType(
                id = company.id.toGraphQLID(),
                name = company.name.value,
                postalCode = company.address.postCode.value,
                prefecture = company.address.prefecture.name,
                city = company.address.city.value,
                streetAddress = company.address.streetAddress.value,
                buildingName = company.address.buildingName?.value,
                createdAt = company.createdAt,
                phoneNumber = company.phoneNumber.value,
                url = company.url?.toString(),
            )
            )}
        )
    }
}

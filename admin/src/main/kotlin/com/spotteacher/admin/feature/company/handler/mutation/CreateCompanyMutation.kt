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
import com.spotteacher.graphql.NonEmptyString
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component
import java.net.URI

data class CreateCompanyMutationInput(
    val name: NonEmptyString,
    val postalCode:  NonEmptyString,
    val prefecture:  NonEmptyString,
    val city:  NonEmptyString,
    val streetAddress:  NonEmptyString,
    val buildingName:  NonEmptyString?,
    val phoneNumber:  NonEmptyString,
    val url:  NonEmptyString?,
)

sealed interface CreateCompanyMutationOutput {
    data class CreateCompanyMutationSuccess(val company: CompanyType) : CreateCompanyMutationOutput
    data class CreateCompanyMutationError(
        val message: String,
        val code: CompanyErrorCode
    ) : CreateCompanyMutationOutput
}

@Component
@PreAuthorize("isAuthenticated()")
class CreateCompanyMutation(
    private val usecase: CreateCompanyUseCase
) : Mutation {
    suspend fun createCompany(input: CreateCompanyMutationInput): CreateCompanyMutationOutput {
        // Create address
        val address = Address(
            postCode = PostCode(input.postalCode.value),
            prefecture = Prefecture.valueOf(input.prefecture.value),
            city = City(input.city.value),
            streetAddress = StreetAddress(input.streetAddress.value),
            buildingName = input.buildingName?.value?.let { BuildingName(it) }
        )

        // Call use case
        val result = usecase.call(
            CreateCompanyUseCaseInput(
                name = CompanyName(input.name.value),
                address = address,
                phoneNumber = PhoneNumber(input.phoneNumber.value),
                url = input.url?.value?.let { URI(it) }
            )
        ).result

        return result.fold(
            ifLeft = { error ->
                CreateCompanyMutationOutput.CreateCompanyMutationError(
                    message = error.message,
                    code = error.code
                )
            },
            ifRight = { company ->
                CreateCompanyMutationOutput.CreateCompanyMutationSuccess(
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
                )
            }
        )
    }
}

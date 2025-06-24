package com.spotteacher.admin.feature.company.handler.mutation

import com.expediagroup.graphql.generator.scalars.ID
import com.expediagroup.graphql.server.operations.Mutation
import com.spotteacher.admin.feature.company.domain.CompanyErrorCode
import com.spotteacher.admin.feature.company.domain.CompanyId
import com.spotteacher.admin.feature.company.domain.CompanyName
import com.spotteacher.admin.feature.company.usecase.UpdateCompanyUseCase
import com.spotteacher.admin.feature.company.usecase.UpdateCompanyUseCaseInput
import com.spotteacher.domain.Address
import com.spotteacher.domain.BuildingName
import com.spotteacher.domain.City
import com.spotteacher.domain.PhoneNumber
import com.spotteacher.domain.PostCode
import com.spotteacher.domain.Prefecture
import com.spotteacher.domain.StreetAddress
import com.spotteacher.graphql.NonEmptyString
import com.spotteacher.graphql.toDomainId
import org.springframework.stereotype.Component
import java.net.URI

data class UpdateCompanyMutationInput(
    val id: ID,
    val name: NonEmptyString?,
    val postalCode: NonEmptyString?,
    val prefecture: NonEmptyString?,
    val city: NonEmptyString?,
    val streetAddress: NonEmptyString?,
    val buildingName: NonEmptyString?,
    val phoneNumber: NonEmptyString,
    val url: NonEmptyString?,
)

sealed interface UpdateCompanyMutationOutput
data class UpdateCompanyMutationSuccess(val result: Unit) : UpdateCompanyMutationOutput
data class UpdateCompanyMutationError(
    val message: String,
    val code: CompanyErrorCode
) : UpdateCompanyMutationOutput

@Component
class UpdateCompanyMutation(
    private val useCase: UpdateCompanyUseCase
) : Mutation {

    suspend fun updateCompany(input: UpdateCompanyMutationInput): UpdateCompanyMutationOutput {
        // Convert ID to CompanyId
        val companyId = input.id.toDomainId(::CompanyId)

        // Create address if any address field is provided
        val address = if (input.postalCode != null || input.prefecture != null || input.city != null || input.streetAddress != null) {
            Address(
                postCode = PostCode(input.postalCode?.value ?: ""),
                prefecture = input.prefecture?.let { Prefecture.valueOf(it.value) } ?: Prefecture.TOKYO,
                city = City(input.city?.value ?: ""),
                streetAddress = StreetAddress(input.streetAddress?.value ?: ""),
                buildingName = input.buildingName?.let { BuildingName(it.value) }
            )
        } else {
            null
        }

        // Call use case
        val result = useCase.call(
            UpdateCompanyUseCaseInput(
                companyId = companyId,
                name = input.name?.let { CompanyName(it.value) },
                address = address,
                phoneNumber = PhoneNumber(input.phoneNumber.value),
                url = input.url?.let { URI(it.value) }
            )
        ).result

        return result.fold(
            ifLeft = { error ->
                UpdateCompanyMutationError(
                    message = error.message,
                    code = error.code
                )
            },
            ifRight = {
                UpdateCompanyMutationSuccess(Unit)
            }
        )
    }
}

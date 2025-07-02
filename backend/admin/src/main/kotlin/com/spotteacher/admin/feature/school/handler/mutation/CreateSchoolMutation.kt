package com.spotteacher.admin.feature.school.handler.mutation

import com.expediagroup.graphql.generator.annotations.GraphQLName
import com.expediagroup.graphql.server.operations.Mutation
import com.spotteacher.admin.feature.school.domain.SchoolCategory
import com.spotteacher.admin.feature.school.domain.SchoolName
import com.spotteacher.admin.feature.school.handler.SchoolType
import com.spotteacher.admin.feature.school.handler.toSchoolType
import com.spotteacher.admin.feature.school.usecase.CreateSchoolUseCase
import com.spotteacher.admin.feature.school.usecase.CreateSchoolUseCaseInput
import com.spotteacher.domain.Address
import com.spotteacher.domain.BuildingName
import com.spotteacher.domain.City
import com.spotteacher.domain.PhoneNumber
import com.spotteacher.domain.PostCode
import com.spotteacher.domain.Prefecture
import com.spotteacher.domain.StreetAddress
import org.springframework.stereotype.Component

@Component
class CreateSchoolMutation(
    private val createSchoolUseCase: CreateSchoolUseCase
) : Mutation {
    suspend fun createSchool(input: CreateSchoolInput): CreateSchoolPayload {
        // Convert input to domain objects
        val schoolName = SchoolName(input.name)
        val schoolCategory = input.schoolCategory
        val phoneNumber = PhoneNumber(input.phoneNumber)

        // Create address
        val address = Address(
            postCode = PostCode(input.postalCode),
            prefecture = input.prefecture,
            city = City(input.city),
            streetAddress = StreetAddress(input.streetAddress),
            buildingName = input.buildingName?.let { BuildingName(it) }
        )

        // Create use case input
        val useCaseInput = CreateSchoolUseCaseInput(
            name = schoolName,
            schoolCategory = schoolCategory,
            address = address,
            phoneNumber = phoneNumber,
            url = input.url
        )

        // Call use case
        val output = createSchoolUseCase.call(useCaseInput)

        // Handle result
        return output.result.fold(
            { error ->
                CreateSchoolPayload(
                    school = null,
                    errors = listOf(error.message)
                )
            },
            { school ->
                CreateSchoolPayload(
                    school = school.toSchoolType(),
                    errors = emptyList()
                )
            }
        )
    }
}

@GraphQLName("CreateSchoolInput")
data class CreateSchoolInput(
    val name: String,
    val schoolCategory: SchoolCategory,
    val city: String,
    val postalCode: String,
    val prefecture: Prefecture,
    val streetAddress: String,
    val buildingName: String?,
    val url: String?,
    val phoneNumber: String
)

@GraphQLName("CreateSchoolPayload")
data class CreateSchoolPayload(
    val school: SchoolType?,
    val errors: List<String>
)

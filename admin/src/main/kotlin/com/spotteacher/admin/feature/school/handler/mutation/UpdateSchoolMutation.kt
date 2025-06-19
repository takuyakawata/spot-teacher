package com.spotteacher.admin.feature.school.handler.mutation

import com.expediagroup.graphql.generator.annotations.GraphQLName
import com.expediagroup.graphql.server.operations.Mutation
import com.spotteacher.admin.feature.school.domain.SchoolCategory
import com.spotteacher.admin.feature.school.domain.SchoolId
import com.spotteacher.admin.feature.school.domain.SchoolName
import com.spotteacher.admin.feature.school.handler.SchoolType
import com.spotteacher.admin.feature.school.handler.toSchoolType
import com.spotteacher.admin.feature.school.usecase.FindSchoolUseCase
import com.spotteacher.admin.feature.school.usecase.FindSchoolUseCaseInput
import com.spotteacher.admin.feature.school.usecase.UpdateSchoolUseCase
import com.spotteacher.admin.feature.school.usecase.UpdateSchoolUseCaseInput
import com.spotteacher.domain.Address
import com.spotteacher.domain.BuildingName
import com.spotteacher.domain.City
import com.spotteacher.domain.PhoneNumber
import com.spotteacher.domain.PostCode
import com.spotteacher.domain.Prefecture
import com.spotteacher.domain.StreetAddress
import org.springframework.stereotype.Component

@Component
class UpdateSchoolMutation(
    private val updateSchoolUseCase: UpdateSchoolUseCase,
    private val findSchoolUseCase: FindSchoolUseCase
) : Mutation {
    suspend fun updateSchool(input: UpdateSchoolInput): UpdateSchoolPayload {
        // Convert input to domain objects
        val schoolId = SchoolId(input.id.toLongOrNull() ?: return UpdateSchoolPayload(
            school = null,
            errors = listOf("Invalid school ID format")
        ))

        // Create use case input with optional fields
        val useCaseInput = UpdateSchoolUseCaseInput(
            schoolId = schoolId,
            name = input.name?.let { SchoolName(it) },
            schoolCategory = input.schoolCategory,
            address = createAddressIfNeeded(input),
            phoneNumber = input.phoneNumber?.let { PhoneNumber(it) },
            url = input.url
        )

        // Call use case
        val output = updateSchoolUseCase.call(useCaseInput)

        // Handle result
        return output.result.fold(
            { error -> 
                UpdateSchoolPayload(
                    school = null,
                    errors = listOf(error.message)
                )
            },
            { 
                // Fetch the updated school to return it
                val findOutput = findSchoolUseCase.call(FindSchoolUseCaseInput(schoolId))
                findOutput.result.fold(
                    { 
                        UpdateSchoolPayload(
                            school = null,
                            errors = listOf("School updated but could not be retrieved")
                        )
                    },
                    { school ->
                        UpdateSchoolPayload(
                            school = school.toSchoolType(),
                            errors = emptyList()
                        )
                    }
                )
            }
        )
    }

    private suspend fun createAddressIfNeeded(input: UpdateSchoolInput): Address? {
        // Only create an Address if at least one address field is provided
        if (input.city == null && input.postalCode == null && input.prefecture == null && 
            input.streetAddress == null && input.buildingName == null) {
            return null
        }

        // We need to fetch the current school to get the existing address fields
        val schoolId = SchoolId(input.id.toLongOrNull() ?: return null)
        val findOutput = findSchoolUseCase.call(FindSchoolUseCaseInput(schoolId))

        val currentSchool = findOutput.result.fold(
            { return null }, // Return null on error
            { it } // Return the school on success
        )

        // Create a new address with the updated fields
        return Address(
            postCode = input.postalCode?.let { PostCode(it) } ?: currentSchool.address.postCode,
            prefecture = input.prefecture ?: currentSchool.address.prefecture,
            city = input.city?.let { City(it) } ?: currentSchool.address.city,
            streetAddress = input.streetAddress?.let { StreetAddress(it) } ?: currentSchool.address.streetAddress,
            buildingName = input.buildingName?.let { BuildingName(it) } ?: currentSchool.address.buildingName
        )
    }
}

@GraphQLName("UpdateSchoolInput")
data class UpdateSchoolInput(
    val id: String,
    val name: String? = null,
    val schoolCategory: SchoolCategory? = null,
    val city: String? = null,
    val postalCode: String? = null,
    val prefecture: Prefecture? = null,
    val streetAddress: String? = null,
    val buildingName: String? = null,
    val url: String? = null,
    val phoneNumber: String? = null
)

@GraphQLName("UpdateSchoolPayload")
data class UpdateSchoolPayload(
    val school: SchoolType?,
    val errors: List<String>
)

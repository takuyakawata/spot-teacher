package com.spotteacher.teacher.feature.school.handler

import com.spotteacher.teacher.feature.school.domain.School


/**
 * Extension function to convert a School domain object to a SchoolType GraphQL object
 */
fun School.toSchoolType(): SchoolType {
    return SchoolType(
        id = id.value.toString(),
        name = name.value,
        schoolCategory = schoolCategory,
        city = address.city.value,
        postalCode = address.postCode.value,
        prefecture = address.prefecture.name,
        streetAddress = address.streetAddress.value,
        buildingName = address.buildingName?.value,
        url = url,
        phoneNumber = phoneNumber.value
    )
}

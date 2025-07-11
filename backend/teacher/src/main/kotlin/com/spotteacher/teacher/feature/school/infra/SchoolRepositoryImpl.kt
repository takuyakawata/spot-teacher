package com.spotteacher.teacher.feature.school.infra

import arrow.core.Nel
import arrow.core.toNonEmptyListOrNull
import com.spotteacher.domain.Address
import com.spotteacher.domain.BuildingName
import com.spotteacher.domain.City
import com.spotteacher.domain.PhoneNumber
import com.spotteacher.domain.PostCode
import com.spotteacher.domain.Prefecture
import com.spotteacher.domain.StreetAddress
import com.spotteacher.extension.nonBlockingFetch
import com.spotteacher.extension.nonBlockingFetchOne
import com.spotteacher.infra.db.enums.SchoolsSchoolCategory
import com.spotteacher.infra.db.tables.Schools.Companion.SCHOOLS
import com.spotteacher.infra.db.tables.records.SchoolsRecord
import com.spotteacher.teacher.feature.school.domain.School
import com.spotteacher.teacher.feature.school.domain.SchoolCategory
import com.spotteacher.teacher.feature.school.domain.SchoolId
import com.spotteacher.teacher.feature.school.domain.SchoolName
import com.spotteacher.teacher.feature.school.domain.SchoolRepository
import com.spotteacher.teacher.shared.infra.TransactionAwareDSLContext
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitLast
import org.springframework.stereotype.Repository

@Repository
class SchoolRepositoryImpl(
    private val dslContext: TransactionAwareDSLContext
) : SchoolRepository {

    override suspend fun getAll(): Nel<School> {
        return  dslContext.get().nonBlockingFetch(SCHOOLS).map { it.toSchoolEntity() }.toNonEmptyListOrNull()!!
    }

    override suspend fun findById(id: SchoolId): School? {
        return dslContext.get().nonBlockingFetchOne(
            SCHOOLS,
            SCHOOLS.ID.eq(id.value)
        )?.toSchoolEntity()
    }

    private fun SchoolsRecord.toSchoolEntity(): School {
        return School(
            id = SchoolId(id!!),
//            schoolCode = SchoolCode("SCH-${id}"),//todo impl yet
            name = SchoolName(name),
            schoolCategory = SchoolCategory.valueOf(schoolCategory.name),
            address = Address(
                postCode = PostCode(postCode),
                prefecture = Prefecture.valueOf(prefecture),
                city = City(city),
                streetAddress = StreetAddress(streetAddress),
                buildingName = buildingName?.let { BuildingName(it) }
            ),
            phoneNumber = PhoneNumber(phoneNumber),
            url = url
        )
    }
}

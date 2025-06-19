package com.spotteacher.admin.feature.school.infra

import arrow.core.Nel
import arrow.core.toNonEmptyListOrNull
import com.spotteacher.admin.feature.school.domain.School
import com.spotteacher.admin.feature.school.domain.SchoolCategory
import com.spotteacher.admin.feature.school.domain.SchoolCode
import com.spotteacher.admin.feature.school.domain.SchoolId
import com.spotteacher.admin.feature.school.domain.SchoolName
import com.spotteacher.admin.feature.school.domain.SchoolRepository
import com.spotteacher.admin.shared.infra.TransactionAwareDSLContext
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
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitLast
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

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

    override suspend fun findByName(name: SchoolName): School? {
        return dslContext.get().nonBlockingFetchOne(
            SCHOOLS,
            SCHOOLS.NAME.eq(name.value)
        )?.toSchoolEntity()
    }

    override suspend fun create(school: School): School {
        val id = dslContext.get().insertInto(SCHOOLS)
            .set(SCHOOLS.NAME, school.name.value)
            .set(SCHOOLS.SCHOOL_CATEGORY, SchoolsSchoolCategory.valueOf(school.schoolCategory.name))
            .set(SCHOOLS.POST_CODE, school.address.postCode.value)
            .set(SCHOOLS.PREFECTURE, school.address.prefecture.name)
            .set(SCHOOLS.CITY, school.address.city.value)
            .set(SCHOOLS.STREET_ADDRESS, school.address.streetAddress.value)
            .set(SCHOOLS.BUILDING_NAME, school.address.buildingName?.value)
            .set(SCHOOLS.PHONE_NUMBER,school.phoneNumber.value)
            .set(SCHOOLS.URL,school.url)
            .returning(SCHOOLS.ID)
            .awaitFirstOrNull()?.id!!

        return school.copy(id = SchoolId(id))
    }

    override suspend fun update(school: School) {
        dslContext.get().update(SCHOOLS)
            .set(SCHOOLS.NAME, school.name.value)
            .set(SCHOOLS.SCHOOL_CATEGORY, SchoolsSchoolCategory.valueOf(school.schoolCategory.name))
            .set(SCHOOLS.POST_CODE, school.address.postCode.value)
            .set(SCHOOLS.PREFECTURE, school.address.prefecture.name)
            .set(SCHOOLS.CITY, school.address.city.value)
            .set(SCHOOLS.STREET_ADDRESS, school.address.streetAddress.value)
            .set(SCHOOLS.BUILDING_NAME, school.address.buildingName?.value)
            .set(SCHOOLS.PHONE_NUMBER,school.phoneNumber.value)
            .set(SCHOOLS.URL,school.url)
            .where(SCHOOLS.ID.eq(school.id.value))
            .awaitLast()
    }

    override suspend fun delete(id: SchoolId) {
        dslContext.get().deleteFrom(SCHOOLS)
            .where(SCHOOLS.ID.eq(id.value))
            .awaitLast()
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

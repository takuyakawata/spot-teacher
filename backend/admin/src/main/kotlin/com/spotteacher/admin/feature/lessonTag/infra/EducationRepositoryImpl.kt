package com.spotteacher.admin.feature.lessonTag.infra

import arrow.core.Nel
import com.spotteacher.admin.feature.lessonTag.domain.Education
import com.spotteacher.admin.feature.lessonTag.domain.EducationId
import com.spotteacher.admin.feature.lessonTag.domain.EducationName
import com.spotteacher.admin.feature.lessonTag.domain.EducationRepository
import com.spotteacher.admin.shared.infra.TransactionAwareDSLContext
import com.spotteacher.extension.nonBlockingFetch
import com.spotteacher.extension.nonBlockingFetchOne
import com.spotteacher.infra.db.tables.Educations.Companion.EDUCATIONS
import com.spotteacher.infra.db.tables.records.EducationsRecord
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitLast
import org.springframework.stereotype.Repository

@Repository
class EducationRepositoryImpl(private val dslContext: TransactionAwareDSLContext) : EducationRepository {
    override suspend fun getAll(): List<Education> {
        val records = dslContext.get().nonBlockingFetch(EDUCATIONS)
        return records.map { it.toEntity() }
    }

    override suspend fun create(education: Education): Education {
        val id = dslContext.get().insertInto(
            EDUCATIONS,
            EDUCATIONS.NAME,
            EDUCATIONS.IS_ACTIVE,
            EDUCATIONS.DISPLAY_ORDER
        ).values(
            education.name.value,
            education.isActive,
            education.displayOrder
        ).returning(EDUCATIONS.ID).awaitFirstOrNull()?.id!!

        return education.copy(id = EducationId(id))
    }

    override suspend fun update(education: Education) {
        dslContext.get().update(EDUCATIONS)
            .set(EDUCATIONS.NAME, education.name.value)
            .set(EDUCATIONS.IS_ACTIVE, education.isActive)
            .set(EDUCATIONS.DISPLAY_ORDER, education.displayOrder)
            .where(EDUCATIONS.ID.eq(education.id.value))
            .awaitLast()
    }

    override suspend fun delete(educationId: EducationId) {
        dslContext.get().deleteFrom(EDUCATIONS)
            .where(EDUCATIONS.ID.eq(educationId.value))
            .awaitLast()
    }

    override suspend fun filterByIds(educationIds: Nel<EducationId>): List<Education> {
        val records = dslContext.get().nonBlockingFetch(
            EDUCATIONS,
            EDUCATIONS.ID.`in`(educationIds.map { it.value })
        )
        return records.map { it.toEntity() }
    }

    override suspend fun findByName(name: EducationName): Education? {
        val record = dslContext.get().nonBlockingFetchOne(
            EDUCATIONS,
            EDUCATIONS.NAME.eq(name.value)
        ) ?: return null

        return record.toEntity()
    }

    private fun EducationsRecord.toEntity(): Education {
        return Education(
            id = EducationId(id!!),
            name = EducationName(name),
            isActive = isActive!!,
            displayOrder = displayOrder!!
        )
    }
}

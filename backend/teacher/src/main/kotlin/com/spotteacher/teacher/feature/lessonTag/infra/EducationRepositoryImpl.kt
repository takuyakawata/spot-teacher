package com.spotteacher.teacher.feature.lessonTag.infra

import arrow.core.Nel
import com.spotteacher.extension.nonBlockingFetch
import com.spotteacher.extension.nonBlockingFetchOne
import com.spotteacher.infra.db.tables.Educations.Companion.EDUCATIONS
import com.spotteacher.infra.db.tables.records.EducationsRecord
import com.spotteacher.teacher.feature.lessonTag.domain.Education
import com.spotteacher.teacher.feature.lessonTag.domain.EducationId
import com.spotteacher.teacher.feature.lessonTag.domain.EducationName
import com.spotteacher.teacher.feature.lessonTag.domain.EducationRepository
import com.spotteacher.teacher.shared.infra.TransactionAwareDSLContext
import org.springframework.stereotype.Repository

@Repository
class EducationRepositoryImpl(private val dslContext: TransactionAwareDSLContext) : EducationRepository {
    override suspend fun getAll(): List<Education> {
        val records = dslContext.get().nonBlockingFetch(EDUCATIONS)
        return records.map { it.toEntity() }
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

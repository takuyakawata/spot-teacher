package com.spotteacher.admin.feature.lessonTag.infra

import arrow.core.Nel
import com.spotteacher.admin.feature.lessonTag.domain.Education
import com.spotteacher.admin.feature.lessonTag.domain.EducationId
import com.spotteacher.admin.feature.lessonTag.domain.EducationName
import com.spotteacher.admin.feature.lessonTag.domain.EducationRepository

class EducationRepositoryImpl : EducationRepository {
    override suspend fun getAll(): List<Education> {
        TODO("Not yet implemented")
    }

    override suspend fun create(education: Education): Education {
        TODO("Not yet implemented")
    }

    override suspend fun update(education: Education) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(educationId: EducationId) {
        TODO("Not yet implemented")
    }

    override suspend fun filerByIds(educationIds: Nel<EducationId>): List<Education> {
        TODO("Not yet implemented")
    }

    override suspend fun findByName(name: EducationName): Education? {
        TODO("Not yet implemented")
    }
}

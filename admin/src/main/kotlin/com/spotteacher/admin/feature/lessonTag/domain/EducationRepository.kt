package com.spotteacher.admin.feature.lessonTag.domain

import arrow.core.Nel

interface EducationRepository {
    suspend fun getAll(): List<Education>
    suspend fun create(education: Education): Education
    suspend fun update(education: Education)
    suspend fun delete(educationId: EducationId)
    suspend fun filerByIds(educationIds: Nel<EducationId>):List<Education>
    suspend fun findByName(name: EducationName): Education?
}

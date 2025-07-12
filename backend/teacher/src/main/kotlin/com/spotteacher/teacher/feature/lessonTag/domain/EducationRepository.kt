package com.spotteacher.teacher.feature.lessonTag.domain

import arrow.core.Nel
import org.jetbrains.annotations.TestOnly

interface EducationRepository {
    suspend fun getAll(): List<Education>
    suspend fun filterByIds(educationIds: Nel<EducationId>): List<Education>
    suspend fun findByName(name: EducationName): Education?

    @TestOnly
    suspend fun create(education: Education): Education
}

package com.spotteacher.teacher.feature.school.domain

import arrow.core.Nel
import org.jetbrains.annotations.TestOnly

interface SchoolRepository {
    suspend fun getAll(): Nel<School>
    suspend fun findById(id: SchoolId): School?

    @TestOnly
    suspend fun create(school: School):School
}

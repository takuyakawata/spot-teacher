package com.spotteacher.teacher.feature.school.domain

import arrow.core.Nel

interface SchoolRepository {
    suspend fun getAll(): Nel<School>
    suspend fun findById(id: SchoolId): School?
}

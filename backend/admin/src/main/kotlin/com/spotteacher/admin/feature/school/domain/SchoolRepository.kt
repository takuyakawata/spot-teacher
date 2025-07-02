package com.spotteacher.admin.feature.school.domain

import arrow.core.Nel

interface SchoolRepository {
    suspend fun getAll(): Nel<School>
    suspend fun findById(id: SchoolId): School?
    suspend fun findByName(name: SchoolName): School?
    suspend fun create(school: School): School
    suspend fun update(school: School)
    suspend fun delete(id: SchoolId)
}

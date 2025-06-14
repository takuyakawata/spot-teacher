package com.spotteacher.domain

import com.spotteacher.admin.shared.graphql.NonEmptyString

interface FeatureFlagRepository {
    suspend fun findByName(name: NonEmptyString): FeatureFlag?
}

package com.spotteacher.domain

import com.spotteacher.graphql.NonEmptyString

interface FeatureFlagRepository {
    suspend fun findByName(name: NonEmptyString): FeatureFlag?
}

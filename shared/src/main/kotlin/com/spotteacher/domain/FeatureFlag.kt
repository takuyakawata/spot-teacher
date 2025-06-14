package com.spotteacher.domain

import com.spotteacher.graphql.NonEmptyString

data class FeatureFlag(
    val name: NonEmptyString,
    val isEnabled: Boolean,
)

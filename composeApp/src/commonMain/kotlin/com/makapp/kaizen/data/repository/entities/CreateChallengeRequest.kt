package com.makapp.kaizen.data.repository.entities

import kotlinx.serialization.Serializable

@Serializable
data class CreateChallengeRequest(
    val name: String,
    val maxFailures: Int,
    val commitment: String?
)
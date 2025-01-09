package org.towny.kaizen.data.repository.entities

data class CreateChallengeRequest(
    val userId: String,
    val name: String,
    val maxFailures: Int
)
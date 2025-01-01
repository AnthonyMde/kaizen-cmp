package org.towny.kaizen.data.repository

data class CreateChallengeRequest(
    val userId: String,
    val name: String,
    val maxFailures: Int
)
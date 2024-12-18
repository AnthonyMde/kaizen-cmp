package org.towny.kaizen.data.remote.dto

import kotlinx.serialization.Serializable
import org.towny.kaizen.domain.models.Challenge

@Serializable
data class ChallengeDTO(
    val id: String,
    val name: String,
    val isCompleted: Boolean,
    val failures: Int,
    val maxFailures: Int
) {
    companion object {
        fun from(challenge: Challenge) = ChallengeDTO(
            id = challenge.id,
            name = challenge.name,
            isCompleted = challenge.isCompleted,
            failures = challenge.failures,
            maxFailures = challenge.maxFailures,
        )
    }

    fun toChallenge() = Challenge(
        id = id,
        name = name,
        isCompleted = isCompleted,
        failures = failures,
        maxFailures = maxFailures
    )
}

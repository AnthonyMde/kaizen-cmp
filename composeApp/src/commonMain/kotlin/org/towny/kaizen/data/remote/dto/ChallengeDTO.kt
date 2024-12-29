package org.towny.kaizen.data.remote.dto

import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.serialization.Serializable
import org.towny.kaizen.domain.models.Challenge
import org.towny.kaizen.utils.DateUtils.toLocalDate

@Serializable
data class ChallengeDTO(
    val id: String,
    val name: String,
    val createdAt: Timestamp,
    val isCompleted: Boolean,
    val failures: Int,
    val maxFailures: Int
) {
    fun toChallenge() = Challenge(
        id = id,
        name = name,
        createdAt = createdAt.toLocalDate(),
        isCompleted = isCompleted,
        failures = failures,
        maxFailures = maxFailures
    )
}

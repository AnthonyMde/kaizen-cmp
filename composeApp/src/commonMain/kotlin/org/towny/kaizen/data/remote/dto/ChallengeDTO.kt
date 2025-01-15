package org.towny.kaizen.data.remote.dto

import kotlinx.serialization.SerialName
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
    @Serializable
    data class Timestamp(
        @SerialName("_seconds")
        val seconds: Long,
        @SerialName("_nanoseconds")
        val nanoseconds: Int
    )

    fun toChallenge() = Challenge(
        id = id,
        name = name,
        createdAt = dev.gitlive.firebase.firestore.Timestamp(
            createdAt.seconds,
            createdAt.nanoseconds
        ).toLocalDate(),
        isCompleted = isCompleted,
        failures = failures,
        maxFailures = maxFailures
    )
}

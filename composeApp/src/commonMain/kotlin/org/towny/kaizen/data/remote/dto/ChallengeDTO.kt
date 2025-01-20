package org.towny.kaizen.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.towny.kaizen.domain.models.Challenge
import org.towny.kaizen.domain.models.Challenge.Status
import org.towny.kaizen.utils.DateUtils.toLocalDate

@Serializable
data class ChallengeDTO(
    val id: String,
    val name: String,
    val status: Status,
    val createdAt: Timestamp,
    val days:  Int,
    val isDoneForToday: Boolean,
    val failureCount: Int,
    val maxAuthorizedFailures: Int
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
        status = status,
        days = days,
        isDoneForToday = isDoneForToday,
        failureCount = failureCount,
        maxAuthorizedFailures = maxAuthorizedFailures
    )
}

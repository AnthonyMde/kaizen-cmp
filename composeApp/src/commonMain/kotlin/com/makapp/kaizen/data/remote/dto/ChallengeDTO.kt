package com.makapp.kaizen.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.makapp.kaizen.domain.models.challenge.Challenge
import com.makapp.kaizen.domain.models.challenge.Challenge.Status
import com.makapp.kaizen.utils.DateUtils.toLocalDate

@Serializable
data class ChallengeDTO(
    val id: String,
    val name: String,
    val status: Status,
    val createdAt: Timestamp,
    val updatedAt: Timestamp = createdAt,
    val days:  Int,
    val isDoneForToday: Boolean,
    val failureCount: Int,
    val maxAuthorizedFailures: Int,
    val isDeleted: Boolean = false,
    val commitment: String?,
    val expectations: String?
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
        updatedAt = dev.gitlive.firebase.firestore.Timestamp(
            updatedAt.seconds,
            updatedAt.nanoseconds
        ).toLocalDate(),
        status = status,
        days = days,
        isDoneForToday = isDoneForToday,
        failureCount = failureCount,
        maxAuthorizedFailures = maxAuthorizedFailures,
        commitment = commitment,
        expectations = expectations
    )
}

package com.makapp.kaizen.data.remote.dto

import com.makapp.kaizen.domain.models.challenge.Challenge
import com.makapp.kaizen.domain.models.challenge.Challenge.Status
import com.makapp.kaizen.utils.DateUtils.toLocalDate
import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
    val expectations: String?,
    val lastFailureDate: Timestamp?,
    val didUseForgotFeatureToday: Boolean = false,
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
        expectations = expectations,
        lastFailureDate = getLastFailureDate(),
        didUseForgotFeatureToday = didUseForgotFeatureToday,
    )

    private fun getLastFailureDate(): LocalDate? {
        lastFailureDate ?: return null

        return dev.gitlive.firebase.firestore.Timestamp(
            lastFailureDate.seconds,
            lastFailureDate.nanoseconds
        ).toLocalDate()
    }
}

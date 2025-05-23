package com.makapp.kaizen.data.remote.dto

import com.makapp.kaizen.data.local.room.challenges.ChallengeEntity
import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.serialization.Serializable
import com.makapp.kaizen.domain.models.challenge.Challenge
import com.makapp.kaizen.domain.models.challenge.Challenge.Status
import com.makapp.kaizen.utils.DateUtils
import com.makapp.kaizen.utils.DateUtils.toLocalDate

@Serializable
data class ChallengeFirestoreDTO(
    val id: String,
    val name: String,
    val status: Status,
    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = createdAt,
    val days: Int,
    val isDoneForToday: Boolean,
    val failureCount: Int,
    val maxAuthorizedFailures: Int,
    val isDeleted: Boolean,
    val commitment: String?,
    val expectations: String?,
    val lastFailureDate: Timestamp?,
    val didUseForgotFeatureToday: Boolean = false,
) {
    fun toChallenge() = Challenge(
        id = id,
        name = name,
        /**
         * /!\ This fallback is very important. Firestore use FieldValue.serverTimestamp to
         * generate the createdAt on the server side, but firestore directly sends an event to
         * the app with the newly created object without the date correctly setup at first.
         * This fallback ensure to wait until we get the right value from the server.
         **/
        createdAt = createdAt?.toLocalDate() ?: DateUtils.getCurrentLocalDate(),
        updatedAt = updatedAt?.toLocalDate() ?: DateUtils.getCurrentLocalDate(),
        status = status,
        days = days,
        isDoneForToday = isDoneForToday,
        failureCount = failureCount,
        maxAuthorizedFailures = maxAuthorizedFailures,
        commitment = commitment,
        expectations = expectations,
        lastFailureDate = lastFailureDate?.toLocalDate(),
        didUseForgotFeatureToday = didUseForgotFeatureToday,
    )

    fun toChallengeEntity(userId: String) = ChallengeEntity(
        id = id,
        userId = userId,
        name = name,
        status = status,
        createdAt = ChallengeDTO.Timestamp(createdAt?.seconds ?: 0, createdAt?.nanoseconds ?: 0),
        updatedAt = ChallengeDTO.Timestamp(updatedAt?.seconds ?: 0, updatedAt?.nanoseconds ?: 0),
        days = days,
        isDoneForToday = isDoneForToday,
        failureCount = failureCount,
        maxAuthorizedFailures = maxAuthorizedFailures,
        isDeleted = isDeleted,
        commitment = commitment,
        expectations = expectations,
        lastFailureDate = ChallengeDTO.Timestamp(lastFailureDate?.seconds ?: 0, lastFailureDate?.nanoseconds ?: 0),
        didUseForgotFeatureToday = didUseForgotFeatureToday,
    )
}

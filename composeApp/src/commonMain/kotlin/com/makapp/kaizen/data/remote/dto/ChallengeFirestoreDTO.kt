package com.makapp.kaizen.data.remote.dto

import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.serialization.Serializable
import com.makapp.kaizen.domain.models.Challenge
import com.makapp.kaizen.domain.models.Challenge.Status
import com.makapp.kaizen.utils.DateUtils
import com.makapp.kaizen.utils.DateUtils.toLocalDate

@Serializable
data class ChallengeFirestoreDTO(
    val id: String,
    val name: String,
    val status: Status,
    val createdAt: Timestamp? = null,
    val days: Int,
    val isDoneForToday: Boolean,
    val failureCount: Int,
    val maxAuthorizedFailures: Int
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
        status = status,
        days = days,
        isDoneForToday = isDoneForToday,
        failureCount = failureCount,
        maxAuthorizedFailures = maxAuthorizedFailures
    )
}

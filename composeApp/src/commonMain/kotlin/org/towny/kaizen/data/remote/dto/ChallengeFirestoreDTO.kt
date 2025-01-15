package org.towny.kaizen.data.remote.dto

import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.serialization.Serializable
import org.towny.kaizen.domain.models.Challenge
import org.towny.kaizen.utils.DateUtils
import org.towny.kaizen.utils.DateUtils.toLocalDate

@Serializable
data class ChallengeFirestoreDTO(
    val id: String,
    val name: String,
    val createdAt: Timestamp? = null,
    val isCompleted: Boolean,
    val failures: Int,
    val maxFailures: Int
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
        isCompleted = isCompleted,
        failures = failures,
        maxFailures = maxFailures
    )
}

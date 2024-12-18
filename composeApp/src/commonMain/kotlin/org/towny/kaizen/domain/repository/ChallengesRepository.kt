package org.towny.kaizen.domain.repository

interface ChallengesRepository {
    suspend fun toggleChallenge(
        userId: String,
        challengeId: String,
        isChecked: Boolean
    )
}
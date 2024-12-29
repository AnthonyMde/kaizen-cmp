package org.towny.kaizen.domain.services

import org.towny.kaizen.domain.repository.ChallengesRepository

class ChallengesService(
    private val challengesRepository: ChallengesRepository,
    private val getUserSessionUseCase: GetUserSessionUseCase
) {
    suspend fun toggleChallenge(
        userId: String,
        challengeId: String,
        isChecked: Boolean
    ) {
        val userSession = getUserSessionUseCase()
        if (userSession == null || userId != userSession) {
            return
        } else {
            challengesRepository.toggleChallenge(userId, challengeId, isChecked)
        }
    }
}
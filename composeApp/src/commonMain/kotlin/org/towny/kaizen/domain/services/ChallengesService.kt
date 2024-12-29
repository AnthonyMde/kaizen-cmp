package org.towny.kaizen.domain.services

import kotlinx.coroutines.flow.first
import org.towny.kaizen.domain.models.Resource
import org.towny.kaizen.domain.repository.ChallengesRepository
import org.towny.kaizen.domain.repository.UsersRepository

class ChallengesService(
    private val challengesRepository: ChallengesRepository,
    private val usersRepository: UsersRepository
) {
    suspend fun toggleChallenge(
        userId: String,
        challengeId: String,
        isChecked: Boolean
    ) {
        val userSession = usersRepository.getSavedUserSession().first()
        if (userSession !is Resource.Success || userSession.data == null || userId != userSession.data) {
            return
        } else {
            challengesRepository.toggleChallenge(userId, challengeId, isChecked)
        }
    }
}
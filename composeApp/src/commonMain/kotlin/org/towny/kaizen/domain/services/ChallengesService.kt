package org.towny.kaizen.domain.services

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.towny.kaizen.domain.exceptions.DomainException
import org.towny.kaizen.domain.models.Resource
import org.towny.kaizen.domain.repository.ChallengesRepository
import org.towny.kaizen.domain.usecases.GetUserSessionUseCase

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

    suspend fun create(name: String, numberOfErrors: String): Flow<Resource<Unit>> {
        if (name.isBlank()) {
            return flowOf(Resource.Error(DomainException.Challenge.HasNoName))
        }
        if (numberOfErrors.isBlank()) {
            return flowOf(Resource.Error(DomainException.Challenge.HasNoMaxErrors))
        }

        return challengesRepository.create(
            name = name,
            numberOfErrors = numberOfErrors.toInt()
        )
    }
}
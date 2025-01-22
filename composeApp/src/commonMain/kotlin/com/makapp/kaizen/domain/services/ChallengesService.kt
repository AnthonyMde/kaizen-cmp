package com.makapp.kaizen.domain.services

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import com.makapp.kaizen.domain.exceptions.DomainException
import com.makapp.kaizen.domain.models.Resource
import com.makapp.kaizen.domain.repository.AuthRepository
import com.makapp.kaizen.domain.repository.ChallengesRepository
import com.makapp.kaizen.domain.usecases.GetReloadedUserSessionUseCase

class ChallengesService(
    private val challengesRepository: ChallengesRepository,
    private val authRepository: AuthRepository,
    private val getReloadedUserSessionUseCase: GetReloadedUserSessionUseCase
) {
    suspend fun toggleChallenge(
        userId: String,
        challengeId: String,
        isChecked: Boolean
    ) {
        val userSession = authRepository.getUserSession()
        if (userSession == null || userId != userSession.uid) {
            return
        } else {
            challengesRepository.toggleStatus(userId, challengeId, isChecked)
        }
    }

    suspend fun create(name: String, numberOfErrors: String): Flow<Resource<Unit>> {
        if (name.isBlank()) {
            return flowOf(Resource.Error(DomainException.Challenge.HasNoName))
        }
        if (numberOfErrors.isBlank()) {
            return flowOf(Resource.Error(DomainException.Challenge.HasNoMaxErrors))
        }

        val userId = getReloadedUserSessionUseCase()?.uid
            ?: return flowOf(Resource.Error(DomainException.User.NoUserSessionFound))

        return challengesRepository.create(
            userId = userId,
            name = name,
            numberOfErrors = numberOfErrors.toInt()
        )
    }
}
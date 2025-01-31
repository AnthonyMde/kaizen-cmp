package com.makapp.kaizen.domain.services

import com.makapp.kaizen.domain.exceptions.DomainException
import com.makapp.kaizen.domain.models.Challenge
import com.makapp.kaizen.domain.models.Resource
import com.makapp.kaizen.domain.repository.AuthRepository
import com.makapp.kaizen.domain.repository.ChallengesRepository
import com.makapp.kaizen.domain.usecases.GetReloadedUserSessionUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

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

        return challengesRepository.create(
            name = name,
            numberOfErrors = numberOfErrors.toInt()
        )
    }

    fun getChallengeById(id: String): Flow<Resource<Challenge>> = flow {
        emit(Resource.Loading())
        val result = challengesRepository.getChallengeById(id)
        emit(result)
    }
}
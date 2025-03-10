package com.makapp.kaizen.domain.repository

import com.makapp.kaizen.domain.models.Challenge
import com.makapp.kaizen.domain.models.CreateChallengeForm
import kotlinx.coroutines.flow.Flow
import com.makapp.kaizen.domain.models.Resource

interface ChallengesRepository {
    suspend fun toggleStatus(
        userId: String,
        challengeId: String,
        isChecked: Boolean
    ): Resource<Unit>
    suspend fun create(
        form: CreateChallengeForm
    ): Flow<Resource<Unit>>
    suspend fun getChallengeById(
        id: String
    ): Resource<Challenge>
}
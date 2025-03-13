package com.makapp.kaizen.domain.repository

import com.makapp.kaizen.domain.models.Challenge
import com.makapp.kaizen.domain.models.CreateChallengeForm
import kotlinx.coroutines.flow.Flow
import com.makapp.kaizen.domain.models.Resource
import com.makapp.kaizen.domain.models.UpdateChallengeFields

interface ChallengesRepository {
    suspend fun toggleStatus(
        userId: String,
        challengeId: String,
        isChecked: Boolean
    ): Resource<Unit>
    suspend fun create(
        form: CreateChallengeForm
    ): Flow<Resource<Unit>>
    suspend fun update(
        id: String,
        fields: UpdateChallengeFields
    ): Flow<Resource<Unit>>
    suspend fun getChallengeById(
        id: String
    ): Resource<Challenge>
}
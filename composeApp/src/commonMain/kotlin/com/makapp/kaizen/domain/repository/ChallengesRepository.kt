package com.makapp.kaizen.domain.repository

import kotlinx.coroutines.flow.Flow
import com.makapp.kaizen.domain.models.Resource

interface ChallengesRepository {
    suspend fun toggleStatus(
        userId: String,
        challengeId: String,
        isChecked: Boolean
    ): Resource<Unit>
    suspend fun create(
        name: String,
        numberOfErrors: Int
    ): Flow<Resource<Unit>>
}
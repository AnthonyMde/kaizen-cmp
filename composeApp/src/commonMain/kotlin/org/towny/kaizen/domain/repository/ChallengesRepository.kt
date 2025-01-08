package org.towny.kaizen.domain.repository

import kotlinx.coroutines.flow.Flow
import org.towny.kaizen.domain.models.Resource

interface ChallengesRepository {
    suspend fun toggleStatus(
        userId: String,
        challengeId: String,
        isChecked: Boolean
    )
    suspend fun create(
        userId: String,
        name: String,
        numberOfErrors: Int
    ): Flow<Resource<Unit>>
}
package com.makapp.kaizen.data.repository.sources

import kotlinx.coroutines.flow.Flow
import com.makapp.kaizen.data.remote.dto.ChallengeFirestoreDTO
import com.makapp.kaizen.data.remote.dto.UserDTO
import com.makapp.kaizen.data.repository.entities.CreateChallengeRequest

interface FirestoreDataSource {
    fun watchCurrentUser(userId: String): Flow<UserDTO?>

    fun watchAllChallenges(userId: String): Flow<List<ChallengeFirestoreDTO>>
    suspend fun toggleChallengeStatus(
        userId: String,
        challengeId: String,
        isChecked: Boolean
    )
    suspend fun createChallenge(request: CreateChallengeRequest)
}

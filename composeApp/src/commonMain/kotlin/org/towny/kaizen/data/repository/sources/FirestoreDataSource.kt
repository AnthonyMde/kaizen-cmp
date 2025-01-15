package org.towny.kaizen.data.repository.sources

import kotlinx.coroutines.flow.Flow
import org.towny.kaizen.data.remote.dto.ChallengeFirestoreDTO
import org.towny.kaizen.data.remote.dto.UserDTO
import org.towny.kaizen.data.repository.entities.CreateChallengeRequest

interface FirestoreDataSource {
    fun watchCurrentUser(userId: String): Flow<UserDTO?>
    suspend fun createUser(userDTO: UserDTO)

    fun watchAllChallenges(userId: String): Flow<List<ChallengeFirestoreDTO>>
    suspend fun toggleChallengeStatus(
        userId: String,
        challengeId: String,
        isChecked: Boolean
    )
    suspend fun createChallenge(request: CreateChallengeRequest)
}

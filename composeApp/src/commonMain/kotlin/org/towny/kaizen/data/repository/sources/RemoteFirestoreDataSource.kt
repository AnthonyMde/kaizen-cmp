package org.towny.kaizen.data.repository.sources

import kotlinx.coroutines.flow.Flow
import org.towny.kaizen.data.remote.dto.ChallengeDTO
import org.towny.kaizen.data.remote.dto.UserDTO
import org.towny.kaizen.data.repository.CreateChallengeRequest
import org.towny.kaizen.domain.models.Resource

interface RemoteFirestoreDataSource {
    fun watchAllUsers(): Flow<List<UserDTO>>
    suspend fun createUser(userDTO: UserDTO): Resource<Unit>

    fun watchAllChallenges(userId: String): Flow<List<ChallengeDTO>>
    suspend fun toggleChallenge(
        userId: String,
        challengeId: String,
        isChecked: Boolean
    )

    suspend fun getUserBy(id: String): UserDTO?

    suspend fun createChallenge(request: CreateChallengeRequest)
}
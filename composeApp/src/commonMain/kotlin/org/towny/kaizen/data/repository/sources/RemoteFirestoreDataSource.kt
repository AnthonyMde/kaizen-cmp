package org.towny.kaizen.data.repository.sources

import kotlinx.coroutines.flow.Flow
import org.towny.kaizen.data.remote.dto.ChallengeDTO
import org.towny.kaizen.data.remote.dto.UserDTO
import org.towny.kaizen.data.repository.entities.CreateChallengeRequest
import org.towny.kaizen.domain.models.FriendRequest

interface RemoteFirestoreDataSource {
    fun watchCurrentUser(userId: String): Flow<UserDTO?>
    fun watchOtherUsers(userId: String): Flow<List<UserDTO>>
    suspend fun createUser(userDTO: UserDTO)
    suspend fun findUserByName(username: String): UserDTO?

    fun watchAllChallenges(userId: String): Flow<List<ChallengeDTO>>
    suspend fun toggleChallengeStatus(
        userId: String,
        challengeId: String,
        isChecked: Boolean
    )
    suspend fun createChallenge(request: CreateChallengeRequest)

    suspend fun createOrUpdateFriendRequest(userId: String, request: FriendRequest)
}

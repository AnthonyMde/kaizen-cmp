package com.makapp.kaizen.data.repository.sources

import com.makapp.kaizen.data.remote.dto.CreateUserRequest
import com.makapp.kaizen.data.remote.dto.FriendDTO
import com.makapp.kaizen.data.remote.dto.IsUsernameAvailableDTO
import com.makapp.kaizen.data.repository.entities.CreateChallengeRequest
import com.makapp.kaizen.domain.models.friend.FriendRequest
import com.makapp.kaizen.domain.models.friend.FriendSearchPreview

interface FirebaseFunctionsDataSource {
    suspend fun isUsernameAvailable(username: String): IsUsernameAvailableDTO
    suspend fun getFriendSearchPreview(username: String): FriendSearchPreview
    suspend fun getFriendRequests(): List<FriendRequest>
    suspend fun createFriendRequest(friendId: String)
    suspend fun updateFriendRequest(requestId: String, status: FriendRequest.Status)
    suspend fun getFriends(includeChallenges: Boolean = true): List<FriendDTO>
    suspend fun createUserAccount(request: CreateUserRequest)
    suspend fun deleteUserAccount()
    suspend fun toggleFriendAsFavorite(friendId: String)
    suspend fun createChallenge(request: CreateChallengeRequest)
    suspend fun updateChallenge(id: String, fieldsToUpdate: Map<String, Any>)
    suspend fun forgotToCheckChallenge(id: String)
}

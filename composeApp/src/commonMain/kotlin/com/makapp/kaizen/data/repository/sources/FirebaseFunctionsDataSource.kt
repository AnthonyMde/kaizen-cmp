package com.makapp.kaizen.data.repository.sources

import com.makapp.kaizen.data.remote.dto.CreateUserRequest
import com.makapp.kaizen.data.remote.dto.FriendDTO
import com.makapp.kaizen.data.remote.dto.IsUsernameAvailableDTO
import com.makapp.kaizen.domain.models.FriendRequest
import com.makapp.kaizen.domain.models.FriendSearchPreview

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
}

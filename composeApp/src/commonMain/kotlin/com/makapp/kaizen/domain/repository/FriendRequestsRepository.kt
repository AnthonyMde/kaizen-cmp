package com.makapp.kaizen.domain.repository

import com.makapp.kaizen.domain.models.FriendRequest
import com.makapp.kaizen.domain.models.Resource

interface FriendRequestsRepository {
    suspend fun getFriendRequests(): Resource<List<FriendRequest>>
    suspend fun createFriendRequest(friendId: String): Resource<Unit>
    suspend fun updateFriendRequest(requestId: String, status: FriendRequest.Status): Resource<Unit>
}

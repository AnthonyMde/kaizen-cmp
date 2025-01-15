package org.towny.kaizen.domain.repository

import org.towny.kaizen.domain.models.FriendRequest
import org.towny.kaizen.domain.models.Resource

interface FriendRequestsRepository {
    suspend fun getFriendRequests(): Resource<List<FriendRequest>>
    suspend fun createFriendRequest(friendId: String): Resource<Unit>
    suspend fun updateFriendRequest(requestId: String, status: FriendRequest.Status): Resource<Unit>
}

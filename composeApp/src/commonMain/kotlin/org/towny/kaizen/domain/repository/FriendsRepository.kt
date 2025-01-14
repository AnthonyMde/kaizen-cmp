package org.towny.kaizen.domain.repository

import org.towny.kaizen.domain.models.FriendPreview
import org.towny.kaizen.domain.models.FriendRequest
import org.towny.kaizen.domain.models.Resource

interface FriendsRepository {
    suspend fun getFriendPreview(username: String): Resource<FriendPreview>
    suspend fun getFriendRequests(): Resource<List<FriendRequest>>
    suspend fun createFriendRequest(friendId: String): Resource<Unit>
}

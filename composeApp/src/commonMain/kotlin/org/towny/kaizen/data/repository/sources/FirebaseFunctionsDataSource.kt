package org.towny.kaizen.data.repository.sources

import org.towny.kaizen.domain.models.Friend
import org.towny.kaizen.domain.models.FriendPreview
import org.towny.kaizen.domain.models.FriendRequest

interface FirebaseFunctionsDataSource {
    suspend fun getFriendPreviewByName(username: String): FriendPreview
    suspend fun getFriendRequests(): List<FriendRequest>
    suspend fun createFriendRequest(friendId: String)
    suspend fun updateFriendRequest(requestId: String, status: FriendRequest.Status)
    suspend fun getFriends(): List<Friend>
}

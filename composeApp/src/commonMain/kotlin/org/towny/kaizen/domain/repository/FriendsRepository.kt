package org.towny.kaizen.domain.repository

import org.towny.kaizen.domain.models.Friend
import org.towny.kaizen.domain.models.FriendPreview
import org.towny.kaizen.domain.models.Resource

interface FriendsRepository {
    suspend fun getFriendPreview(username: String): Resource<FriendPreview>
    suspend fun getFriendPreviews(): Resource<List<FriendPreview>>
    suspend fun getFriends(): Resource<List<Friend>>
}

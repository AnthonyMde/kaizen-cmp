package com.makapp.kaizen.domain.repository

import com.makapp.kaizen.domain.models.Friend
import com.makapp.kaizen.domain.models.FriendPreview
import com.makapp.kaizen.domain.models.Resource
import kotlinx.coroutines.flow.Flow

interface FriendsRepository {
    suspend fun getFriendPreview(username: String): Resource<FriendPreview>
    fun watchFriendPreviews(): Flow<Resource<List<FriendPreview>>>
    suspend fun refreshFriendPreviews(): Resource<Unit>
    suspend fun getFriends(): Resource<List<Friend>>
}

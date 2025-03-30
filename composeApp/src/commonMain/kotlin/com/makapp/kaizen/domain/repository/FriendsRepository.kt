package com.makapp.kaizen.domain.repository

import com.makapp.kaizen.domain.models.friend.Friend
import com.makapp.kaizen.domain.models.Resource
import kotlinx.coroutines.flow.Flow

interface FriendsRepository {
    fun watchFriends(): Flow<Resource<List<Friend>>>
    suspend fun refreshFriends(): Resource<Unit>
    suspend fun toggleFriendAsFavorite(friendId: String): Resource<Unit>
}

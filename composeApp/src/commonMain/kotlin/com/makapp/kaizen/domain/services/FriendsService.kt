package com.makapp.kaizen.domain.services

import com.makapp.kaizen.domain.models.Friend
import com.makapp.kaizen.domain.models.FriendPreview
import com.makapp.kaizen.domain.models.Resource
import com.makapp.kaizen.domain.repository.FriendsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FriendsService(
    private val friendsRepository: FriendsRepository
) {
    fun getFriends(): Flow<Resource<List<Friend>>> = flow {
        emit(Resource.Loading())
        emit(friendsRepository.getFriends())
    }

    fun watchFriendPreviews(): Flow<Resource<List<FriendPreview>>> {
        return friendsRepository.watchFriendPreviews()
    }

    suspend fun refreshFriendPreviews() {
        friendsRepository.refreshFriendPreviews()
    }
}

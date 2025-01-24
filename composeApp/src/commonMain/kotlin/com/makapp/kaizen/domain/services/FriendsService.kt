package com.makapp.kaizen.domain.services

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.makapp.kaizen.domain.models.Friend
import com.makapp.kaizen.domain.models.FriendPreview
import com.makapp.kaizen.domain.models.Resource
import com.makapp.kaizen.domain.repository.FriendsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FriendsService(
    private val friendsRepository: FriendsRepository
) {
    private val _isRefreshingPreviews = MutableStateFlow(false)
    val isRefreshingPreviews = _isRefreshingPreviews.asStateFlow()

    fun getFriends(): Flow<Resource<List<Friend>>> = flow {
        emit(Resource.Loading())
        emit(friendsRepository.getFriends())
    }

    fun watchFriendPreviews(): Flow<Resource<List<FriendPreview>>> {
        return friendsRepository.watchFriendPreviews()
    }

    suspend fun refreshFriendPreviews() {
        _isRefreshingPreviews.update { true }
        friendsRepository.refreshFriendPreviews()
        _isRefreshingPreviews.update { false }
    }
}

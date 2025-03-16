package com.makapp.kaizen.domain.services

import com.makapp.kaizen.domain.models.Friend
import com.makapp.kaizen.domain.models.Resource
import com.makapp.kaizen.domain.repository.FriendPreviewsRepository
import com.makapp.kaizen.domain.repository.FriendsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class FriendsService(
    private val friendsRepository: FriendsRepository,
    private val friendPreviewsRepository: FriendPreviewsRepository
) {
    private val _isRefreshFriendsLoading = MutableStateFlow(false)
    val isRefreshFriendsLoading = _isRefreshFriendsLoading.asStateFlow()

    fun watchFavoriteFriends(): Flow<Resource<List<Friend>>> {
        return friendsRepository.watchFriends().map { result ->
            if (result is Resource.Success) {
                val friends = result.data
                    ?.filter { it.isFavorite }
                    ?.map { friend ->
                        friend.copy(challenges = friend.challenges.filter { !it.isFailed() && !it.isAbandoned() })
                    }
                Resource.Success(friends)
            } else {
                result
            }
        }
    }

    suspend fun refreshFriends() {
        _isRefreshFriendsLoading.update { true }
        val result = friendsRepository.refreshFriends()
        if (result !is Resource.Loading) {
            _isRefreshFriendsLoading.update { false }
        }
    }

    fun toggleFriendAsFavorite(friendId: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        val result = friendsRepository.toggleFriendAsFavorite(friendId)
        if (result is Resource.Success) {
            friendPreviewsRepository.refreshFriendPreviews()
        }
        emit(result)
    }
}

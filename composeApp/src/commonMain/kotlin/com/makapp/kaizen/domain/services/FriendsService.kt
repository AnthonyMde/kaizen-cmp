package com.makapp.kaizen.domain.services

import com.makapp.kaizen.domain.models.Friend
import com.makapp.kaizen.domain.models.Resource
import com.makapp.kaizen.domain.repository.FriendsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FriendsService(
    private val friendsRepository: FriendsRepository
) {
    private val _isRefreshFriendsLoading = MutableStateFlow(false)
    val isRefreshFriendsLoading = _isRefreshFriendsLoading.asStateFlow()

    fun watchFriends(): Flow<Resource<List<Friend>>> {
        return friendsRepository.watchFriends()
    }

    suspend fun refreshFriends() {
        _isRefreshFriendsLoading.update { true }
        friendsRepository.refreshFriends()
        _isRefreshFriendsLoading.update { false }
    }
}

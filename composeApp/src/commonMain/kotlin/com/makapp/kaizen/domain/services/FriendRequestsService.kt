package com.makapp.kaizen.domain.services

import com.makapp.kaizen.domain.exceptions.DomainException
import com.makapp.kaizen.domain.models.FriendRequest
import com.makapp.kaizen.domain.models.Resource
import com.makapp.kaizen.domain.repository.FriendRequestsRepository
import com.makapp.kaizen.domain.repository.UsersRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FriendRequestsService(
    private val friendRequestsRepository: FriendRequestsRepository,
    private val usersRepository: UsersRepository,
    private val friendsService: FriendsService,
    private val scope: CoroutineScope
) {
    private val _isFriendRequestsRefreshing = MutableStateFlow(false)
    val isFriendRequestsRefreshing = _isFriendRequestsRefreshing.asStateFlow()

    fun watchFriendRequests(): Flow<Resource<List<FriendRequest>>> {
        return friendRequestsRepository.watchFriendRequests()
    }

    suspend fun refreshFriendRequests() {
        _isFriendRequestsRefreshing.update { true }
        friendRequestsRepository.refreshFriendRequests()
        _isFriendRequestsRefreshing.update { false }
    }

    fun updateFriendRequest(requestId: String, status: FriendRequest.Status): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())

        val result = friendRequestsRepository.updateFriendRequest(requestId, status)
        if (result is Resource.Success) {
            scope.launch { refreshFriendRequests() }
            if (status == FriendRequest.Status.ACCEPTED) {
                scope.launch { friendsService.refreshFriendPreviews() }
            }
        }

        emit(result)
    }

    suspend fun createFriendRequest(friendId: String?): Resource<Unit> {
        friendId ?: return Resource.Error(DomainException.Common.InvalidArguments)
        val userId = usersRepository.getCurrentUser()?.id
        if (userId == friendId) {
            return Resource.Error(DomainException.Friend.CannotSendFriendRequestToYourself)
        }

        val result = friendRequestsRepository.createFriendRequest(friendId)
        if (result is Resource.Success) {
            scope.launch { refreshFriendRequests() }
        }

        return result
    }
}

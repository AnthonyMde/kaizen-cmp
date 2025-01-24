package com.makapp.kaizen.domain.services

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.makapp.kaizen.domain.exceptions.DomainException
import com.makapp.kaizen.domain.models.FriendRequest
import com.makapp.kaizen.domain.models.Resource
import com.makapp.kaizen.domain.repository.FriendRequestsRepository
import com.makapp.kaizen.domain.repository.UsersRepository

class FriendRequestsService(
    private val friendRequestsRepository: FriendRequestsRepository,
    private val usersRepository: UsersRepository
) {
    fun watchFriendRequests(): Flow<Resource<List<FriendRequest>>> {
        return friendRequestsRepository.watchFriendRequests()
    }

    fun refreshFriendRequests(): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        emit(friendRequestsRepository.refreshFriendRequests())
    }

    fun updateFriendRequest(requestId: String, status: FriendRequest.Status): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        emit(friendRequestsRepository.updateFriendRequest(requestId, status))
    }

    suspend fun createFriendRequest(friendId: String?): Resource<Unit> {
        friendId ?: return Resource.Error(DomainException.Common.InvalidArguments)
        val userId = usersRepository.getCurrentUser()?.id
        if (userId == friendId) {
            return Resource.Error(DomainException.Friend.CannotSendFriendRequestToYourself)
        }
        return friendRequestsRepository.createFriendRequest(friendId)
    }
}

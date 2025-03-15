package com.makapp.kaizen.domain.services

import com.makapp.kaizen.domain.exceptions.DomainException
import com.makapp.kaizen.domain.models.FriendRequest
import com.makapp.kaizen.domain.models.Resource
import com.makapp.kaizen.domain.repository.FriendRequestsRepository
import com.makapp.kaizen.domain.repository.UsersRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FriendRequestsService(
    private val friendRequestsRepository: FriendRequestsRepository,
    private val usersRepository: UsersRepository,
    private val friendPreviewsService: FriendPreviewsService,
    private val scope: CoroutineScope
) {
    fun watchFriendRequests(): Flow<Resource<List<FriendRequest>>> {
        return friendRequestsRepository.watchFriendRequests()
    }

    suspend fun refreshFriendRequests() {
        friendRequestsRepository.refreshFriendRequests()
    }

    fun updateFriendRequest(requestId: String, status: FriendRequest.Status): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())

        val result = friendRequestsRepository.updateFriendRequest(requestId, status)
        if (result is Resource.Success) {
            refreshFriendRequests()
            if (status == FriendRequest.Status.ACCEPTED) {
                friendPreviewsService.refreshFriendPreviews()
            }
        }

        emit(result)
    }

    suspend fun createFriendRequest(friendId: String?): Resource<Unit> {
        friendId ?: return Resource.Error(DomainException.Common.InvalidArguments())

        if (usersRepository.getUser()?.id == friendId) {
            return Resource.Error(DomainException.Friend.CannotSendFriendRequestToYourself)
        }

        val result = friendRequestsRepository.createFriendRequest(friendId)
        if (result is Resource.Success) {
            refreshFriendRequests()
        }

        return result
    }
}

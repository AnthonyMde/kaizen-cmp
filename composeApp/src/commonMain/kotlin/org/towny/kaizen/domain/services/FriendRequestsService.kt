package org.towny.kaizen.domain.services

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.towny.kaizen.domain.exceptions.DomainException
import org.towny.kaizen.domain.models.FriendRequest
import org.towny.kaizen.domain.models.Resource
import org.towny.kaizen.domain.repository.FriendRequestsRepository
import org.towny.kaizen.domain.repository.UsersRepository

class FriendRequestsService(
    private val friendRequestsRepository: FriendRequestsRepository,
    private val usersRepository: UsersRepository
) {
    suspend fun getFriendRequests(): Resource<List<FriendRequest>> {
        return friendRequestsRepository.getFriendRequests()
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

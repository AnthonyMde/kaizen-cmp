package org.towny.kaizen.domain.services

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.towny.kaizen.domain.exceptions.DomainException
import org.towny.kaizen.domain.models.FriendPreview
import org.towny.kaizen.domain.models.FriendRequest
import org.towny.kaizen.domain.models.Resource
import org.towny.kaizen.domain.repository.FriendsRepository
import org.towny.kaizen.domain.repository.UsersRepository

class FriendsService(
    private val friendsRepository: FriendsRepository,
    private val usersRepository: UsersRepository
) {
    fun getFriendPreview(username: String): Flow<Resource<FriendPreview>> = flow {
        if (username.isBlank()) {
            emit(Resource.Error(DomainException.Common.InvalidArguments))
            return@flow
        }
        val currentUsername = usersRepository.getCurrentUser()?.name
        if (currentUsername == username) {
            emit(Resource.Error(DomainException.Friend.CannotSearchForYourself))
            return@flow
        }

        emit(Resource.Loading())
        val result = friendsRepository.getFriendPreview(username)
        emit(result)
    }

    suspend fun getFriendRequests(): Resource<List<FriendRequest>> {
        return friendsRepository.getFriendRequests()
    }

    suspend fun createFriendRequest(friendId: String?): Resource<Unit> {
        friendId ?: return Resource.Error(DomainException.Common.InvalidArguments)
        val userId = usersRepository.getCurrentUser()?.id
        if (userId == friendId) {
            return Resource.Error(DomainException.Friend.CannotSendFriendRequestToYourself)
        }
        return friendsRepository.createFriendRequest(friendId)
    }
}

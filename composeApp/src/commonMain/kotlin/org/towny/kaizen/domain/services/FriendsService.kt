package org.towny.kaizen.domain.services

import org.towny.kaizen.domain.exceptions.DomainException
import org.towny.kaizen.domain.models.FriendPreview
import org.towny.kaizen.domain.models.FriendRequest
import org.towny.kaizen.domain.models.FriendRequestProfile
import org.towny.kaizen.domain.models.Resource
import org.towny.kaizen.domain.repository.FriendsRepository
import org.towny.kaizen.domain.repository.UsersRepository

class FriendsService(
    private val friendsRepository: FriendsRepository,
    private val usersRepository: UsersRepository
) {
    suspend fun getFriendPreview(username: String): Resource<FriendPreview> {
        if (username.isBlank()) {
            return Resource.Error(DomainException.Common.InvalidArguments)
        }
        return friendsRepository.getFriendPreview(username)
    }

    suspend fun createFriendRequest(friendId: String): Resource<Unit> {
        val username = usersRepository.getCurrentUser()?.name ?:
            return Resource.Error(DomainException.User.NoUserAccountFound)

        return updateFriendRequest(FriendRequest(
            sender = FriendRequestProfile(username),
            receiver = FriendRequestProfile(friendId),
            state = FriendRequest.State.PENDING
        ))
    }

    suspend fun updateFriendRequest(request: FriendRequest): Resource<Unit> {
        return friendsRepository.createOrUpdateFriendRequest(request)
    }
}

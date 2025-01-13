package org.towny.kaizen.domain.services

import org.towny.kaizen.domain.exceptions.DomainException
import org.towny.kaizen.domain.models.FriendPreview
import org.towny.kaizen.domain.models.Resource
import org.towny.kaizen.domain.repository.FriendsRepository

class FriendsService(
    private val friendsRepository: FriendsRepository,
) {
    suspend fun getFriendPreview(username: String): Resource<FriendPreview> {
        if (username.isBlank()) {
            return Resource.Error(DomainException.Common.InvalidArguments)
        }
        return friendsRepository.getFriendPreview(username)
    }

    suspend fun createFriendRequest(friendId: String?): Resource<Unit> {
        friendId ?: return Resource.Error(DomainException.Common.InvalidArguments)
        return friendsRepository.createFriendRequest(friendId)
    }
}

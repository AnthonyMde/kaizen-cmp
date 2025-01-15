package org.towny.kaizen.domain.services

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.towny.kaizen.domain.models.Friend
import org.towny.kaizen.domain.models.Resource
import org.towny.kaizen.domain.repository.FriendsRepository

class FriendsService(
    private val friendsRepository: FriendsRepository
) {
    fun getFriends(): Flow<Resource<List<Friend>>> = flow {
        emit(Resource.Loading())
        emit(friendsRepository.getFriends())
    }
}

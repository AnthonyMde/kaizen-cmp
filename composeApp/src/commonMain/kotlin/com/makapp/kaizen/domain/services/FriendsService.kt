package com.makapp.kaizen.domain.services

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.makapp.kaizen.domain.models.Friend
import com.makapp.kaizen.domain.models.FriendPreview
import com.makapp.kaizen.domain.models.Resource
import com.makapp.kaizen.domain.repository.FriendsRepository

class FriendsService(
    private val friendsRepository: FriendsRepository
) {
    fun getFriends(): Flow<Resource<List<Friend>>> = flow {
        emit(Resource.Loading())
        emit(friendsRepository.getFriends())
    }

    fun getFriendPreviews(): Flow<Resource<List<FriendPreview>>> = flow {
        emit(Resource.Loading())
        emit(friendsRepository.getFriendPreviews())
    }
}

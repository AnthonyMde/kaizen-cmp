package com.makapp.kaizen.domain.usecases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.makapp.kaizen.domain.exceptions.DomainException
import com.makapp.kaizen.domain.models.FriendPreview
import com.makapp.kaizen.domain.models.Resource
import com.makapp.kaizen.domain.repository.FriendPreviewsRepository
import com.makapp.kaizen.domain.repository.FriendsRepository
import com.makapp.kaizen.domain.repository.UsersRepository

class GetFriendPreviewUseCase(
    private val usersRepository: UsersRepository,
    private val friendPreviewsRepository: FriendPreviewsRepository
) {
    operator fun invoke(username: String): Flow<Resource<FriendPreview>> = flow {
        val trimmedUsername = username.trim()
        if (trimmedUsername.isBlank()) {
            emit(Resource.Error(DomainException.Common.InvalidArguments))
            return@flow
        }
        val currentUsername = usersRepository.getCurrentUser()?.name
        if (currentUsername == trimmedUsername) {
            emit(Resource.Error(DomainException.Friend.CannotSearchForYourself))
            return@flow
        }

        emit(Resource.Loading())
        val result = friendPreviewsRepository.getFriendPreview(trimmedUsername)
        emit(result)
    }
}

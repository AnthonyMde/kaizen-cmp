package com.makapp.kaizen.domain.usecases

import com.makapp.kaizen.domain.exceptions.DomainException
import com.makapp.kaizen.domain.models.FriendSearchPreview
import com.makapp.kaizen.domain.models.Resource
import com.makapp.kaizen.domain.repository.FriendPreviewsRepository
import com.makapp.kaizen.domain.repository.UsersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetFriendPreviewUseCase(
    private val usersRepository: UsersRepository,
    private val friendPreviewsRepository: FriendPreviewsRepository
) {
    operator fun invoke(username: String): Flow<Resource<FriendSearchPreview>> = flow {
        val trimmedUsername = username.trim()
        if (trimmedUsername.isBlank()) {
            emit(Resource.Error(DomainException.Common.InvalidArguments()))
            return@flow
        }
        val currentUsername = usersRepository.getUser()?.name
        if (currentUsername == trimmedUsername) {
            emit(Resource.Error(DomainException.Friend.CannotSearchForYourself))
            return@flow
        }

        emit(Resource.Loading())
        val result = friendPreviewsRepository.getFriendSearchPreview(trimmedUsername)
        emit(result)
    }
}

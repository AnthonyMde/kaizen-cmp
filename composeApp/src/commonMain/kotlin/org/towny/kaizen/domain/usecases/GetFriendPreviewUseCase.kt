package org.towny.kaizen.domain.usecases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.towny.kaizen.domain.exceptions.DomainException
import org.towny.kaizen.domain.models.FriendPreview
import org.towny.kaizen.domain.models.Resource
import org.towny.kaizen.domain.repository.FriendsRepository
import org.towny.kaizen.domain.repository.UsersRepository

class GetFriendPreviewUseCase(
    private val usersRepository: UsersRepository,
    private val friendsRepository: FriendsRepository
) {
    operator fun invoke(username: String): Flow<Resource<FriendPreview>> = flow {
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
}

package org.towny.kaizen.domain.usecases

import org.towny.kaizen.domain.exceptions.DomainException
import org.towny.kaizen.domain.models.Resource
import org.towny.kaizen.domain.models.User
import org.towny.kaizen.domain.repository.UsersRepository

class CreateUserUseCase(
    private val usersRepository: UsersRepository,
    private val getReloadedUserSessionUseCase: GetReloadedUserSessionUseCase,
    private val isUsernameAvailableUseCase: IsUsernameAvailableUseCase
) {
    suspend operator fun invoke(params: CreateUserParams): Resource<Unit> {
        val session = getReloadedUserSessionUseCase()
            ?: return Resource.Error(DomainException.User.NoUserSessionFound)

        val isAvailableResult = isUsernameAvailableUseCase(params.username)
        if (isAvailableResult is Resource.Error) {
            return Resource.Error(isAvailableResult.throwable)
        }

        return usersRepository.createUser(
            User(
                id = session.uid,
                name = params.username,
                profilePictureIndex = params.pictureProfileIndex,
                challenges = emptyList()
            )
        )
    }
}

data class CreateUserParams(
    val username: String, val pictureProfileIndex: Int
)

package com.makapp.kaizen.domain.usecases

import com.makapp.kaizen.data.remote.dto.CreateUserRequest
import com.makapp.kaizen.domain.exceptions.DomainException
import com.makapp.kaizen.domain.models.Resource
import com.makapp.kaizen.domain.repository.UsersRepository

class CreateUserUseCase(
    private val usersRepository: UsersRepository,
    private val getReloadedUserSessionUseCase: GetReloadedUserSessionUseCase,
    private val verifyUsernameAvailableUseCase: VerifyUsernameAvailableUseCase,
    private val verifyUsernameFormatUseCase: VerifyUsernameFormatUseCase,
    private val verifyDisplayNameUseCase: VerifyDisplayNameUseCase
) {
    suspend operator fun invoke(params: CreateUserParams): Resource<Unit> {
        val session = getReloadedUserSessionUseCase()
            ?: return Resource.Error(DomainException.User.NoUserSessionFound)

        val username = params.username.trim()
        val displayName = params.displayName.trim()

        val usernameFormatCheckResult = verifyUsernameFormatUseCase(username)
        if (usernameFormatCheckResult is Resource.Error) {
            return Resource.Error(usernameFormatCheckResult.throwable)
        }

        val displayNameFormatCheckResult = verifyDisplayNameUseCase(displayName)
        if (displayNameFormatCheckResult is Resource.Error) {
            return Resource.Error(displayNameFormatCheckResult.throwable)
        }

        val availableCheckResult = verifyUsernameAvailableUseCase(username)
        if (availableCheckResult is Resource.Error) {
            return Resource.Error(availableCheckResult.throwable)
        }

        return usersRepository.createUser(
            CreateUserRequest(
                email = session.email,
                name = username,
                displayName = displayName,
                profilePictureIndex = params.pictureProfileIndex,
            )
        )
    }
}

data class CreateUserParams(
    val username: String,
    val displayName: String,
    val pictureProfileIndex: Int
)

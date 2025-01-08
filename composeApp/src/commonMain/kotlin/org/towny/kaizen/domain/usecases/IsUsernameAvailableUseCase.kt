package org.towny.kaizen.domain.usecases

import org.towny.kaizen.domain.exceptions.DomainException
import org.towny.kaizen.domain.models.Resource
import org.towny.kaizen.domain.repository.UsersRepository

class IsUsernameAvailableUseCase(
    private val usersRepository: UsersRepository
) {
    suspend operator fun invoke(username: String): Resource<Unit> {
        val isAvailableResult = usersRepository.isUsernameAvailable(username)

        if (isAvailableResult is Resource.Error) {
            return Resource.Error(DomainException.Auth.UsernameCannotBeVerified)
        }

        return if (isAvailableResult is Resource.Success && isAvailableResult.data != true) {
            Resource.Error(DomainException.Auth.UsernameAlreadyUsed)
        } else {
            Resource.Success()
        }
    }
}

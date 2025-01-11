package org.towny.kaizen.domain.usecases

import org.towny.kaizen.domain.exceptions.DomainException
import org.towny.kaizen.domain.models.Resource
import org.towny.kaizen.domain.repository.UsersRepository

class VerifyUsernameAvailableUseCase(
    private val usersRepository: UsersRepository
) {
    suspend operator fun invoke(username: String): Resource<Unit> {
        val isAvailableResult = usersRepository.isUsernameAvailable(username)

        if (isAvailableResult is Resource.Error) {
            return Resource.Error(DomainException.User.Name.CannotBeVerified)
        }

        return if (isAvailableResult is Resource.Success && isAvailableResult.data != true) {
            Resource.Error(DomainException.User.Name.AlreadyUsed)
        } else {
            Resource.Success()
        }
    }
}

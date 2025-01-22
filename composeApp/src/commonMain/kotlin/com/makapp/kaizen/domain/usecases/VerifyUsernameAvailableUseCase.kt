package com.makapp.kaizen.domain.usecases

import com.makapp.kaizen.domain.exceptions.DomainException
import com.makapp.kaizen.domain.models.Resource
import com.makapp.kaizen.domain.repository.UsersRepository

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

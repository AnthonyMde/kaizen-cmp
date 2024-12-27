package org.towny.kaizen.domain.services

import kotlinx.coroutines.flow.first
import org.towny.kaizen.domain.models.Resource
import org.towny.kaizen.domain.repository.UsersRepository

class GetUserSessionUseCase(private val usersRepository: UsersRepository) {
    suspend operator fun invoke(): String? {
        return when (val result = usersRepository.getSavedUsername().first()) {
            is Resource.Error -> null
            is Resource.Loading -> null
            is Resource.Success -> result.data
        }
    }
}

package org.towny.kaizen.domain.services

import kotlinx.coroutines.flow.first
import org.towny.kaizen.domain.models.Resource
import org.towny.kaizen.domain.repository.UsersRepository

suspend fun getUserSession(usersRepository: UsersRepository): String? {
    return when (val result = usersRepository.getSavedUsername().first()) {
        is Resource.Error -> null
        is Resource.Loading -> null
        is Resource.Success -> result.data
    }
}

package org.towny.kaizen.domain.services

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.towny.kaizen.domain.models.Resource
import org.towny.kaizen.domain.models.User
import org.towny.kaizen.domain.repository.UsersRepository

class UsersService(
    private val usersRepository: UsersRepository
) {
    fun watchMe(): Flow<Resource<User?>> = usersRepository.watchCurrentUser()

    suspend fun getMe(): User? = usersRepository.getCurrentUser()

    fun deleteUserAccount(): Flow<Resource<Unit>> = flow{
        emit(Resource.Loading())
        emit(usersRepository.deleteUserAccount())
    }
}

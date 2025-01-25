package com.makapp.kaizen.domain.services

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.makapp.kaizen.domain.models.Resource
import com.makapp.kaizen.domain.models.User
import com.makapp.kaizen.domain.repository.UsersRepository

class UsersService(
    private val usersRepository: UsersRepository
) {
    fun watchMe(): Flow<Resource<User?>> = usersRepository.watchCurrentUser()

    suspend fun getUser(): User? = usersRepository.getUser()

    fun deleteUserAccount(): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        emit(usersRepository.deleteUserAccount())
    }
}

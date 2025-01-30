package com.makapp.kaizen.domain.repository

import com.makapp.kaizen.data.remote.dto.CreateUserRequest
import kotlinx.coroutines.flow.Flow
import com.makapp.kaizen.domain.models.Resource
import com.makapp.kaizen.domain.models.User

interface UsersRepository {
    fun watchCurrentUser(): Flow<Resource<User?>>
    suspend fun createUser(request: CreateUserRequest): Resource<Unit>
    suspend fun getUser(): User?
    suspend fun isUsernameAvailable(username: String): Resource<Boolean>
    suspend fun deleteUserAccount(): Resource<Unit>
}

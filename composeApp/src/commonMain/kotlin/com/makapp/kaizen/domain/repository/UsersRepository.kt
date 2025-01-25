package com.makapp.kaizen.domain.repository

import kotlinx.coroutines.flow.Flow
import com.makapp.kaizen.domain.models.Resource
import com.makapp.kaizen.domain.models.User

interface UsersRepository {
    fun watchCurrentUser(): Flow<Resource<User?>>
    suspend fun createUser(user: User): Resource<Unit>
    suspend fun getUser(): User?
    suspend fun isUsernameAvailable(username: String): Resource<Boolean>
    suspend fun deleteUserAccount(): Resource<Unit>
}

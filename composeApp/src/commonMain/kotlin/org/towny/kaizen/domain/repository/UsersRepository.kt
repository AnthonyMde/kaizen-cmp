package org.towny.kaizen.domain.repository

import kotlinx.coroutines.flow.Flow
import org.towny.kaizen.domain.models.Resource
import org.towny.kaizen.domain.models.User

interface UsersRepository {
    fun watchMe(): Flow<Resource<User?>>
    fun watchFriends(): Flow<Resource<List<User>>>
    suspend fun createUser(user: User): Resource<Unit>
    suspend fun getCurrentUser(): User?
}

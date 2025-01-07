package org.towny.kaizen.domain.repository

import kotlinx.coroutines.flow.Flow
import org.towny.kaizen.domain.models.Resource
import org.towny.kaizen.domain.models.User

interface UsersRepository {
    val watchAll: Flow<Resource<List<User>>>
    fun getSavedUserSession(): Flow<Resource<String?>>
    suspend fun deleteSavedUserSession()
    suspend fun createUser(user: User): Resource<Unit>
}

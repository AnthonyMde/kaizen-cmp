package org.towny.kaizen.domain.repository

import kotlinx.coroutines.flow.Flow
import org.towny.kaizen.domain.models.Resource
import org.towny.kaizen.domain.models.User

interface UsersRepository {
    val watchAll: Flow<Resource<List<User>>>
    suspend fun getSavedUser(): Resource<User>
}
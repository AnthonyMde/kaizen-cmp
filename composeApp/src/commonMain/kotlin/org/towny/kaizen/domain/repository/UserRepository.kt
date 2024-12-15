package org.towny.kaizen.domain.repository

import kotlinx.coroutines.flow.Flow
import org.towny.kaizen.domain.entities.User

interface UserRepository {
    fun getUsers(): Flow<List<User>>
}
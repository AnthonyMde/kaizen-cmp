package org.towny.kaizen.domain.repository

import kotlinx.coroutines.flow.Flow
import org.towny.kaizen.domain.models.Resource

interface LoginRepository {
    fun login(username: String): Flow<Resource<Unit>>
}
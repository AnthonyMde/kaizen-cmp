package org.towny.kaizen.data.repository.sources

import org.towny.kaizen.domain.models.User

interface LocalPreferencesDataSource {
    suspend fun getSavedUser(): User?
    suspend fun saveUser(user: User)
}
package org.towny.kaizen.data.local

import org.towny.kaizen.data.repository.sources.LocalPreferencesDataSource
import org.towny.kaizen.domain.models.User

class LocalPreferencesDataSourceImpl: LocalPreferencesDataSource {
    override suspend fun getSavedUser(): User? {
        // TODO
        return null
    }

    override suspend fun saveUser(user: User) {
        // TODO
    }

}
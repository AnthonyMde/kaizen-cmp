package org.towny.kaizen.data.repository.sources

import kotlinx.coroutines.flow.Flow

interface LocalPreferencesDataSource {
    fun getSavedUsername(): Flow<String?>
    suspend fun saveUserId(userId: String)
    suspend fun deleteUserId()
}

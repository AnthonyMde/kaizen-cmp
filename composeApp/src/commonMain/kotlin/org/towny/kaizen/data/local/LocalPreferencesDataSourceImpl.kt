package org.towny.kaizen.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.towny.kaizen.data.repository.sources.LocalPreferencesDataSource

class LocalPreferencesDataSourceImpl(private val dataStore: DataStore<Preferences>) :
    LocalPreferencesDataSource {
    companion object {
        private const val USER_SESSION_KEY = "session_user"
        private val userIdPreferenceKey = stringPreferencesKey(USER_SESSION_KEY)
    }

    override fun getSavedUsername(): Flow<String?> =
        dataStore.data.map { preferences ->
            preferences[userIdPreferenceKey]
        }

    override suspend fun saveUserId(userId: String) {
        dataStore.edit { preferences ->
            preferences[userIdPreferenceKey] = userId
        }
    }

    override suspend fun deleteUserId() {
        dataStore.edit { preferences ->
            preferences.remove(userIdPreferenceKey)
        }
    }
}

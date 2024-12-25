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
        private const val USERNAME_KEY = "session_username"
        private val usernamePreferenceKey = stringPreferencesKey(USERNAME_KEY)
    }

    override fun getSavedUsername(): Flow<String?> =
        dataStore.data.map { preferences ->
            preferences[usernamePreferenceKey]
        }

    override suspend fun saveUsername(username: String) {
        dataStore.edit { preferences ->
            preferences[usernamePreferenceKey] = username
        }
    }
}
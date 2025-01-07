package org.towny.kaizen.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import org.towny.kaizen.data.repository.sources.LocalPreferencesDataSource

class LocalPreferencesDataSourceImpl(private val dataStore: DataStore<Preferences>) :
    LocalPreferencesDataSource {
    companion object {
        private const val USER_SESSION_KEY = "session_user"
        private val userIdPreferenceKey = stringPreferencesKey(USER_SESSION_KEY)
    }
}

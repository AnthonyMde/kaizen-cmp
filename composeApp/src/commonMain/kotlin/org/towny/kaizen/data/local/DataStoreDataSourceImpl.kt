package org.towny.kaizen.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey

class DataStoreDataSourceImpl(private val dataStore: DataStore<Preferences>) :
    org.towny.kaizen.data.repository.sources.DataStoreDataSource {
    companion object {
        private const val USER_SESSION_KEY = "session_user"
        private val userIdPreferenceKey = stringPreferencesKey(USER_SESSION_KEY)
    }
}

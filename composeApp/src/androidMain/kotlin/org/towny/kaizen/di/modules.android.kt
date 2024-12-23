package org.towny.kaizen.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import org.koin.core.module.Module
import org.koin.dsl.module
import org.towny.kaizen.data.local.createDataStore

actual val targetModule: Module = module {
    single<DataStore<Preferences>> { createDataStore(context = get()) }
}
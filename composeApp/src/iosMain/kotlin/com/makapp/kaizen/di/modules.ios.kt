package com.makapp.kaizen.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import createDataStore
import org.koin.core.module.Module
import org.koin.dsl.module

actual val targetModule: Module = module {
    single<DataStore<Preferences>> { createDataStore() }
}
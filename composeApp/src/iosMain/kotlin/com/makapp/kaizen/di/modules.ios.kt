package com.makapp.kaizen.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.makapp.kaizen.data.local.room.AppDatabase
import com.makapp.kaizen.data.local.room.getRoomDatabase
import com.makapp.kaizen.data.room.getDatabaseBuilder
import createDataStore
import org.koin.core.module.Module
import org.koin.dsl.module

actual val targetModule: Module = module {
    single<DataStore<Preferences>> { createDataStore() }
    single<AppDatabase> {
        getRoomDatabase(
            builder = getDatabaseBuilder()
        )
    }
}
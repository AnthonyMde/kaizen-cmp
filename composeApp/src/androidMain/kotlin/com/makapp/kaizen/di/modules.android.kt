package com.makapp.kaizen.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import org.koin.core.module.Module
import org.koin.dsl.module
import com.makapp.kaizen.data.local.createDataStore
import com.makapp.kaizen.data.local.room.AppDatabase
import com.makapp.kaizen.data.local.room.getRoomDatabase
import com.makapp.kaizen.data.room.getDatabaseBuilder

actual val targetModule: Module = module {
    single<DataStore<Preferences>> { createDataStore(context = get()) }
    single<AppDatabase> {
        getRoomDatabase(
            builder = getDatabaseBuilder(context = get())
        )
    }
}
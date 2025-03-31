package com.makapp.kaizen.di

import com.makapp.kaizen.data.local.DataStoreDataSourceImpl
import com.makapp.kaizen.data.remote.firebase_auth.FirebaseAuthDataSourceImpl
import com.makapp.kaizen.data.remote.firebase_functions.FirebaseFunctionsDataSourceImpl
import com.makapp.kaizen.data.remote.firestore.RemoteFirestoreDataSourceImpl
import com.makapp.kaizen.data.repository.sources.DataStoreDataSource
import com.makapp.kaizen.data.repository.sources.FirebaseAuthDataSource
import com.makapp.kaizen.data.repository.sources.FirebaseFunctionsDataSource
import com.makapp.kaizen.data.repository.sources.FirestoreDataSource
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataSourceModule = module {
    singleOf(::DataStoreDataSourceImpl).bind<DataStoreDataSource>()
    singleOf(::RemoteFirestoreDataSourceImpl).bind<FirestoreDataSource>()
    singleOf(::FirebaseAuthDataSourceImpl).bind<FirebaseAuthDataSource>()
    singleOf(::FirebaseFunctionsDataSourceImpl).bind<FirebaseFunctionsDataSource>()
}
package com.makapp.kaizen.di


import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.Module
import org.koin.dsl.module

val commonModule = module {
    single<CoroutineScope> {
        CoroutineScope(Dispatchers.IO + SupervisorJob())
    }
}

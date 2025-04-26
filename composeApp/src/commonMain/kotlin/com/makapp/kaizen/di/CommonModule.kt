package com.makapp.kaizen.di


import com.makapp.kaizen.app.feedback.AppFeedbackManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module

val commonModule = module {
    single<CoroutineScope> {
        CoroutineScope(Dispatchers.IO + SupervisorJob())
    }
    single { AppFeedbackManager() }
}

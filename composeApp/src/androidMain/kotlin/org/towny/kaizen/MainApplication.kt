package org.towny.kaizen

import android.app.Application
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.towny.kaizen.di.initKoin
import org.towny.kaizen.domain.models.UserSession
import org.towny.kaizen.domain.usecases.GetReloadedUserSessionUseCase

class MainApplication : Application() {
    var userSession: UserSession? = null
        private set

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@MainApplication)
        }

        FirebaseApp.initializeApp(this)

        runBlocking {
            val getReloadedUserSessionUseCase = getKoin().get<GetReloadedUserSessionUseCase>()
            userSession = getReloadedUserSessionUseCase()
        }
    }
}

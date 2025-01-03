package org.towny.kaizen

import android.app.Application
import com.google.firebase.FirebaseApp
import dev.gitlive.firebase.auth.FirebaseUser
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.towny.kaizen.di.initKoin
import org.towny.kaizen.domain.usecases.InitializeUserUseCase

class MainApplication : Application() {
    var user: FirebaseUser? = null
        private set

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@MainApplication)
        }

        FirebaseApp.initializeApp(this)

        runBlocking {
            val initializeUserUseCase = getKoin().get<InitializeUserUseCase>()
            user = initializeUserUseCase()
        }
    }
}

package org.towny.kaizen

import android.app.Application
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.towny.kaizen.di.initKoin
import org.towny.kaizen.domain.repository.UsersRepository
import org.towny.kaizen.domain.services.getUserSession

class MainApplication : Application() {
    var username: String? = null
        private set

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@MainApplication)
        }

        runBlocking {
            val usersRepository = getKoin().get<UsersRepository>()
            username = getUserSession(usersRepository)
        }
    }
}
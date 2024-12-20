package org.towny.kaizen

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.towny.kaizen.di.initKoin

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@MainApplication)
            // You can add only android specific modules here.
        }
    }
}
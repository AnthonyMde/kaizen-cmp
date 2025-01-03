package org.towny.kaizen

import androidx.compose.ui.window.ComposeUIViewController
import dev.gitlive.firebase.auth.FirebaseUser
import kotlinx.coroutines.runBlocking
import org.koin.mp.KoinPlatform
import org.towny.kaizen.app.App
import org.towny.kaizen.di.initKoin
import org.towny.kaizen.domain.usecases.InitializeUserUseCase

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    var user: FirebaseUser?
    runBlocking {
        val initializeSessionUseCase = KoinPlatform.getKoin().get<InitializeUserUseCase>()
        user = initializeSessionUseCase()
    }

    App(user = user)
}

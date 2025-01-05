package org.towny.kaizen

import androidx.compose.ui.window.ComposeUIViewController
import kotlinx.coroutines.runBlocking
import org.koin.mp.KoinPlatform
import org.towny.kaizen.app.App
import org.towny.kaizen.di.initKoin
import org.towny.kaizen.domain.models.UserSession
import org.towny.kaizen.domain.usecases.GetReloadedUserSessionUseCase

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    var user: UserSession?
    runBlocking {
        val initializeSessionUseCase = KoinPlatform.getKoin().get<GetReloadedUserSessionUseCase>()
        user = initializeSessionUseCase()
    }

    App(user = user)
}

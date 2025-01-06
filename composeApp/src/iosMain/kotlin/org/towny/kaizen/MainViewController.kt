package org.towny.kaizen

import androidx.compose.ui.window.ComposeUIViewController
import kotlinx.coroutines.runBlocking
import org.koin.mp.KoinPlatform
import org.towny.kaizen.app.App
import org.towny.kaizen.di.initKoin
import org.towny.kaizen.domain.models.UserSession
import org.towny.kaizen.domain.repository.AuthRepository

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    var session: UserSession?
    runBlocking {
        val authRepository = KoinPlatform.getKoin().get<AuthRepository>()
        session = authRepository.getUserSession()
    }

    App(userSession = session)
}

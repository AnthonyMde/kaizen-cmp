package org.towny.kaizen

import androidx.compose.ui.window.ComposeUIViewController
import kotlinx.coroutines.runBlocking
import org.koin.mp.KoinPlatform
import org.towny.kaizen.app.App
import org.towny.kaizen.di.initKoin
import org.towny.kaizen.domain.repository.UsersRepository
import org.towny.kaizen.domain.services.GetUserSessionUseCase

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    var username: String?
    runBlocking {
        val usersRepository = KoinPlatform.getKoin().get<UsersRepository>()
        username = GetUserSessionUseCase(usersRepository).invoke()
    }
    App(username = username)
}

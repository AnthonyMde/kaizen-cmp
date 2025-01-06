package org.towny.kaizen

import androidx.compose.ui.window.ComposeUIViewController
import org.towny.kaizen.app.App
import org.towny.kaizen.di.initKoin
import org.towny.kaizen.domain.app.initializeUserSession

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    App(userSession = initializeUserSession())
}

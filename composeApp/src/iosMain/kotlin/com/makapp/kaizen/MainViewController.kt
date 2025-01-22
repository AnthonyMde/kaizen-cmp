package com.makapp.kaizen

import androidx.compose.ui.window.ComposeUIViewController
import com.makapp.kaizen.app.App
import com.makapp.kaizen.di.initKoin
import com.makapp.kaizen.domain.app.initializeUserSession

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    App(userSession = initializeUserSession())
}

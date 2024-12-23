package org.towny.kaizen

import androidx.compose.ui.window.ComposeUIViewController
import org.towny.kaizen.app.App
import org.towny.kaizen.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) { App() }
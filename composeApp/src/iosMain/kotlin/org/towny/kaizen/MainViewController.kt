package org.towny.kaizen

import androidx.compose.ui.window.ComposeUIViewController
import org.towny.kaizen.di.initKoin
import org.towny.kaizen.ui.App

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) { App() }
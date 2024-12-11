package org.towny.kaizen

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.towny.kaizen.ui.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Kaizen",
    ) {
        App()
    }
}
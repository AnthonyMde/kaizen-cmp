package org.towny.kaizen.ui

import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.towny.kaizen.ui.screens.home.HomeScreen
import org.towny.kaizen.ui.theme.AppTheme

@Composable
@Preview
fun App() {
    AppTheme {
        HomeScreen()
    }
}
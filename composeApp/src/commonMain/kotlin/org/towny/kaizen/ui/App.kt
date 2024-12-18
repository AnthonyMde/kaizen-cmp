package org.towny.kaizen.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.towny.kaizen.ui.screens.home.HomeScreenRoot
import org.towny.kaizen.ui.theme.AppTheme

@Composable
@Preview
fun App() {
    AppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface)
                .systemBarsPadding()
        ) {
            HomeScreenRoot()
        }
    }
}
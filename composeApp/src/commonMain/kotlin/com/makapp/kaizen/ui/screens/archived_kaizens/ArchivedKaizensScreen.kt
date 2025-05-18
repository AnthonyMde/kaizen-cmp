package com.makapp.kaizen.ui.screens.archived_kaizens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.makapp.kaizen.ui.components.BackTopAppBar

@Composable
fun ArchivedKaizensScreenRoot(
    onNavigateUp: () -> Unit,
) {
    ArchivedKaizensScreen(
        onAction = { action ->
            when (action) {
                ArchivedKaizensAction.OnNavigateUp -> {
                    onNavigateUp()
                }
            }
        }
    )
}

@Composable
fun ArchivedKaizensScreen(
    onAction: (ArchivedKaizensAction) -> Unit,
) {
    Scaffold(
        topBar = {
            BackTopAppBar(
                title = "Mes kaizens archivés", // TODO
                onNavigateUp = { onAction(ArchivedKaizensAction.OnNavigateUp) },
                backDescription = "Go back to account", // TODO
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .padding(top = 24.dp)
        ) {
            // TODO
            Text("Archived Kaizen screen")
        }
    }
}
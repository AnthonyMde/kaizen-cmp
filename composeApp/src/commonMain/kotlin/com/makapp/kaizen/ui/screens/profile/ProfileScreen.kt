package com.makapp.kaizen.ui.screens.profile

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
fun ProfileScreenRoot(
    onNavigateUp: () -> Unit,
) {
    ProfileScreen(
        onAction = { action ->
            when (action) {
                ProfileAction.OnNavigateUp -> onNavigateUp()
            }
        }
    )
}

@Composable
fun ProfileScreen(
    onAction: (ProfileAction) -> Unit,
) {
    Scaffold(
        topBar = {
            BackTopAppBar(
                title = "My profile", // TODO
                onNavigateUp = { onAction(ProfileAction.OnNavigateUp) },
                backDescription = "Back to account", // TODO
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
            Text("Profile screen")
        }
    }
}
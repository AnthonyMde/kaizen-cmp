package org.towny.kaizen.ui.screens.create_challenge

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import org.towny.kaizen.ui.screens.components.BackTopAppBar

@Composable
fun CreateChallengeScreenRoot(
    viewModel: CreateChallengeViewModel = koinViewModel(),
    navigateUp: () -> Unit
) {
    val state by viewModel.createChallengeScreenState.collectAsState()
    CreateChallengeScreen(
        state = state,
        onAction = { action ->
            when (action) {
                CreateChallengeAction.OnNavigateUp -> navigateUp()
            }
        }
    )
}

@Composable
fun CreateChallengeScreen(
    state: CreateChallengeScreenState,
    onAction: (CreateChallengeAction) -> Unit
) {
    Scaffold(
        topBar = {
            BackTopAppBar(
                title = "Create challenge",
                onNavigateUp = {
                    onAction(CreateChallengeAction.OnNavigateUp)
                },
                backDescription = "Go back to account."
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                onValueChange = {},
                value = state.challengeTitleInputValue,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}
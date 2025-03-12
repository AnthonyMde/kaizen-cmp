package com.makapp.kaizen.ui.screens.create_challenge.step2

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.makapp.kaizen.ui.screens.components.BackTopAppBar
import com.makapp.kaizen.ui.screens.create_challenge.CreateChallengeViewModel
import org.koin.compose.koinInject

@Composable
fun CreateChallengeCommitmentScreen() {
    val keyboard = LocalSoftwareKeyboardController.current
    val viewModel = koinInject<CreateChallengeViewModel>()

    println("DEBUG: CommitmentScreen title: ${viewModel.createChallengeScreenState.value.challengeNameInputValue}")
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            BackTopAppBar(
                title = "Create challenge",
                onNavigateUp = {
                    // TODO
                },
                backDescription = "Go back to account.",
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Your Kaizen journey is a 365-day adventure. Keep it simple - small, consistent steps make all the difference.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
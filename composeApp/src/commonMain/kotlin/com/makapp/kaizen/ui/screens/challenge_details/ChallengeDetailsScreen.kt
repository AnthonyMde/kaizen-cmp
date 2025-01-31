package com.makapp.kaizen.ui.screens.challenge_details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.makapp.kaizen.ui.screens.components.BackTopAppBar
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ChallengeDetailsScreenRoot(
    viewModel: ChallengeDetailsViewModel = koinViewModel(),
    navigateUp: () -> Unit,
    title: String,
    id: String
) {
    LifecycleEventEffect(Lifecycle.Event.ON_START) {
        viewModel.fetchChallengeDetails(id)
    }

    ChallengeDetailsScreen(
        title = title,
        onAction = { action ->
            when (action) {
                ChallengeDetailsAction.OnNavigateUp -> navigateUp()
                else -> viewModel.onAction(action)
            }
        }
    )
}

@Composable
fun ChallengeDetailsScreen(
    title: String,
    onAction: (ChallengeDetailsAction) -> Unit,
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            BackTopAppBar(
                title = "",
                onNavigateUp = { onAction(ChallengeDetailsAction.OnNavigateUp) },
                backDescription = "Go back.",
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text(title,
                modifier = Modifier.fillMaxWidth())
        }
    }
}

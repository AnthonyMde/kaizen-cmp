package com.makapp.kaizen.ui.screens.challenge_details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
    val state by viewModel.state.collectAsState()

    LifecycleEventEffect(Lifecycle.Event.ON_START) {
        viewModel.fetchChallengeDetails(id)
    }

    ChallengeDetailsScreen(
        title = title,
        state = state,
        viewModel = viewModel,
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
    state: ChallengeDetailsState,
    viewModel: ChallengeDetailsViewModel,
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
                .padding(horizontal = 24.dp)
        ) {
            Text(title,
                modifier = Modifier.fillMaxWidth())

            if (state.isDetailsLoading) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .padding(top = 24.dp)
                        .padding(horizontal = 24.dp)
                )
            }
            if (state.challengeError != null) {
                Text(state.challengeError,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.fillMaxWidth())
            }
            if (state.challenge != null && !state.isDetailsLoading) {
                val challenge = state.challenge
                Text("Challenge created at: ${challenge.createdAt}",
                    modifier = Modifier.fillMaxWidth())

                Text("You have completed your challenge for ${challenge.days} days",
                    modifier = Modifier.fillMaxWidth())

                Text(viewModel.getChallengeStatusText(challenge.status),
                    modifier = Modifier.fillMaxWidth())

                Text("Errors used : ${challenge.failureCount}/${challenge.maxAuthorizedFailures}",
                    modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

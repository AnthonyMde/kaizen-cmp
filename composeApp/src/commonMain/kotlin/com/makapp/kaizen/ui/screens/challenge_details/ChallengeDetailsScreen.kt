package com.makapp.kaizen.ui.screens.challenge_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.makapp.kaizen.ui.screens.challenge_details.components.ChallengeDetailsCommitmentView
import com.makapp.kaizen.ui.screens.challenge_details.components.ChallengeDetailsDashboardCard
import com.makapp.kaizen.ui.screens.components.BackTopAppBar
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ChallengeDetailsScreenRoot(
    viewModel: ChallengeDetailsViewModel = koinViewModel(),
    navigateUp: () -> Unit,
    navArgs: ChallengeDetailsNavArgs
) {
    val state by viewModel.state.collectAsState()

    LifecycleEventEffect(Lifecycle.Event.ON_START) {
        viewModel.fetchChallengeDetails(navArgs.id)
    }

    ChallengeDetailsScreen(
        navArgs = navArgs,
        state = state,
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
    navArgs: ChallengeDetailsNavArgs,
    state: ChallengeDetailsState,
    onAction: (ChallengeDetailsAction) -> Unit,
) {
    Scaffold(
        topBar = {
            BackTopAppBar(
                title = navArgs.title,
                onNavigateUp = { onAction(ChallengeDetailsAction.OnNavigateUp) },
                backDescription = "Go back",
                actions = {
                    IconButton(
                        onClick = { },
                        content = {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Challenge settings",
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        }
                    )
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface)
                .padding(innerPadding)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                if (state.isDetailsLoading) {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
                if (state.challengeError != null) {
                    Text(
                        state.challengeError,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                if (state.challenge != null && !state.isDetailsLoading) {
                    val challenge = state.challenge

                    ChallengeDetailsDashboardCard(
                        challenge = challenge
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ChallengeDetailsCommitmentView(
                        commitmentText = state.challenge.commitment
                    )
                }
            }
        }
    }
}

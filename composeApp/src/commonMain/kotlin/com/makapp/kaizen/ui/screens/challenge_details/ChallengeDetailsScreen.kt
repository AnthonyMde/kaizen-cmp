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
import com.makapp.kaizen.ui.screens.challenge_details.components.ChallengeDetailsClickableTextBoxView
import com.makapp.kaizen.ui.screens.challenge_details.components.ChallengeDetailsDashboardCard
import com.makapp.kaizen.ui.screens.components.BackTopAppBar
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ChallengeDetailsScreenRoot(
    viewModel: ChallengeDetailsViewModel = koinViewModel(),
    navArgs: ChallengeDetailsNavArgs,
    navigateUp: () -> Unit,
    goToChallengeExpectations: (String) -> Unit,
    goToChallengeCommitment: (String) -> Unit,
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
                is ChallengeDetailsAction.GoToChallengeExpectations ->
                    goToChallengeExpectations(action.expectations)

                is ChallengeDetailsAction.GoToChallengeCommitment ->
                    goToChallengeCommitment(action.commitment)
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
                    if (!navArgs.readOnly) {
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
                        challenge = challenge,
                        readOnly = navArgs.readOnly
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ChallengeDetailsClickableTextBoxView(
                        title = "Minimum commitment \uD83D\uDD25",
                        text = state.challenge.commitment,
                        emptyViewTitle = "Minimum commitment \uD83D\uDD25",
                        emptyViewText = "Specify your minimum daily commitment here.",
                        readOnly = navArgs.readOnly,
                        onClick = {
                            onAction(
                                ChallengeDetailsAction.GoToChallengeCommitment(
                                    state.challenge.commitment ?: ""
                                )
                            )
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ChallengeDetailsClickableTextBoxView(
                        title = "My expectations \uD83E\uDDD8\u200D♂\uFE0F",
                        text = state.challenge.expectations,
                        emptyViewTitle = "My expectations \uD83E\uDDD8\u200D♂\uFE0F",
                        emptyViewText = "Specify what do you expect from this 365-days challenge.",
                        readOnly = navArgs.readOnly,
                        onClick = {
                            onAction(
                                ChallengeDetailsAction.GoToChallengeExpectations(
                                    expectations = state.challenge.expectations ?: ""
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}

package com.makapp.kaizen.ui.screens.challenge_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.makapp.kaizen.ui.screens.challenge_details.components.ChallengeDetailsCommitmentView
import com.makapp.kaizen.ui.screens.challenge_details.components.ChallengeDetailsFirstRowView
import com.makapp.kaizen.ui.screens.challenge_details.components.ChallengeDetailsHeaderView
import com.makapp.kaizen.ui.screens.challenge_details.components.ChallengeDetailsSecondRowView
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
    navArgs: ChallengeDetailsNavArgs,
    state: ChallengeDetailsState,
    viewModel: ChallengeDetailsViewModel,
    onAction: (ChallengeDetailsAction) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface)
    ) {
        ChallengeDetailsHeaderView(
            navArgs = navArgs,
            onAction = onAction
        )

        Spacer(modifier = Modifier.height(24.dp))

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

                ChallengeDetailsFirstRowView(
                    challengeStatus = viewModel.getChallengeStatusText(challenge.status),
                    challengeDays = state.challenge.days
                )

                Spacer(modifier = Modifier.height(8.dp))

                ChallengeDetailsSecondRowView(
                    failureCount = state.challenge.failureCount,
                    maxAuthorizedFailures = state.challenge.maxAuthorizedFailures
                )

                Spacer(modifier = Modifier.height(36.dp))

                ChallengeDetailsCommitmentView()
            }
        }
    }
}

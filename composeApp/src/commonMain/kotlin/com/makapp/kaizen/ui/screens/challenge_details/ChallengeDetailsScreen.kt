package com.makapp.kaizen.ui.screens.challenge_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.makapp.kaizen.ui.components.BackTopAppBar
import com.makapp.kaizen.ui.screens.challenge_details.components.ChallengeDetailsClickableTextBoxView
import com.makapp.kaizen.ui.screens.challenge_details.components.ChallengeDetailsDashboardCard
import com.makapp.kaizen.ui.screens.challenge_details.components.ChallengeDetailsDropDownMenu
import com.makapp.kaizen.ui.screens.challenge_details.components.ChangeChallengeStatusBottomSheet
import com.makapp.kaizen.ui.screens.challenge_details.components.ChangeChallengeStatusModalView
import com.makapp.kaizen.ui.screens.challenge_details.components.DeleteChallengeModalView
import com.makapp.kaizen.ui.screens.challenge_details.components.ForgotChallengeButtonView
import kaizen.composeapp.generated.resources.Res
import kaizen.composeapp.generated.resources.challenge_details_expectations_emptyview_subtitle
import kaizen.composeapp.generated.resources.challenge_details_expectations_emptyview_title
import kaizen.composeapp.generated.resources.challenge_details_expectations_title
import kaizen.composeapp.generated.resources.challenge_details_min_commitment_emptyview_subtitle
import kaizen.composeapp.generated.resources.challenge_details_min_commitment_emptyview_title
import kaizen.composeapp.generated.resources.challenge_details_min_commitment_title
import kaizen.composeapp.generated.resources.challenge_details_screen_back_description
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ChallengeDetailsScreenRoot(
    viewModel: ChallengeDetailsViewModel = koinViewModel(),
    navArgs: ChallengeDetailsNavArgs,
    navigateUp: () -> Unit,
    goToChallengeInfos: (Int, String) -> Unit,
    goToChallengeExpectations: (String) -> Unit,
    goToChallengeCommitment: (String) -> Unit,
) {
    val state by viewModel.state.collectAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(null) {
        scope.launch {
            viewModel.event.collectLatest { event ->
                when (event) {
                    ChallengeDetailsEvents.NavigateUp -> navigateUp()
                }
            }
        }
    }

    LifecycleEventEffect(Lifecycle.Event.ON_START) {
        viewModel.watchChallengeDetails(navArgs.id)
    }

    ChallengeDetailsScreen(
        state = state,
        isEditable = viewModel.isChallengeEditable(state.challenge, navArgs.readOnly),
        onAction = { action ->
            when (action) {
                ChallengeDetailsAction.OnNavigateUp -> navigateUp()
                is ChallengeDetailsAction.GoToChallengeInfos ->
                    goToChallengeInfos(action.maxLives, action.title)

                is ChallengeDetailsAction.GoToChallengeExpectations ->
                    goToChallengeExpectations(action.expectations)

                is ChallengeDetailsAction.GoToChallengeCommitment ->
                    goToChallengeCommitment(action.commitment)

                else -> viewModel.onAction(action)
            }
        }
    )
}

@Composable
fun ChallengeDetailsScreen(
    isEditable: Boolean,
    state: ChallengeDetailsState,
    onAction: (ChallengeDetailsAction) -> Unit,
) {
    val scroll = rememberScrollState()

    Scaffold(
        topBar = {
            BackTopAppBar(
                title = state.challenge?.name ?: "",
                onNavigateUp = { onAction(ChallengeDetailsAction.OnNavigateUp) },
                backDescription = stringResource(Res.string.challenge_details_screen_back_description),
                actions = {
                    if (isEditable) {
                        state.challenge?.let {
                            ChallengeDetailsDropDownMenu(it, onAction)
                        }
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scroll)
                .background(color = MaterialTheme.colorScheme.surface)
                .padding(innerPadding)
                .padding(bottom = 24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
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
                        text = stringResource(state.challengeError),
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                if (state.challenge != null && !state.isDetailsLoading) {
                    ChallengeDetailsDashboardCard(
                        challenge = state.challenge,
                        isEditable = isEditable,
                        onAction = onAction
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ChallengeDetailsClickableTextBoxView(
                        title = stringResource(Res.string.challenge_details_min_commitment_title),
                        text = state.challenge.commitment,
                        emptyViewTitle = stringResource(Res.string.challenge_details_min_commitment_emptyview_title),
                        emptyViewText = stringResource(Res.string.challenge_details_min_commitment_emptyview_subtitle),
                        readOnly = !isEditable,
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
                        title = stringResource(Res.string.challenge_details_expectations_title),
                        text = state.challenge.expectations,
                        emptyViewTitle = stringResource(Res.string.challenge_details_expectations_emptyview_title),
                        emptyViewText = stringResource(Res.string.challenge_details_expectations_emptyview_subtitle),
                        readOnly = !isEditable,
                        onClick = {
                            onAction(
                                ChallengeDetailsAction.GoToChallengeExpectations(
                                    expectations = state.challenge.expectations ?: ""
                                )
                            )
                        }
                    )

                    if (state.shouldDisplayForgotToCheckButton) {
                        ForgotChallengeButtonView(
                            challengeId = state.challenge.id,
                            isLoading = state.isForgotToCheckButtonLoading,
                            onAction = onAction,
                        )
                    }

                    if (state.isBottomSheetOpened) {
                        ChangeChallengeStatusBottomSheet(onAction, state.challenge.status)
                    }

                    if (state.isChangeStatusModalDisplayed && state.newStatusRequested != null) {
                        ChangeChallengeStatusModalView(
                            newRequestedStatus = state.newStatusRequested,
                            challengeId = state.challenge.id,
                            isLoading = state.isChangeStatusRequestLoading,
                            error = state.changeStatusRequestError?.let { stringResource(it) },
                            onAction = onAction
                        )
                    }

                    if (state.isDeleteChallengeModalDisplayed) {
                        DeleteChallengeModalView(
                            challengeId = state.challenge.id,
                            isLoading = state.isDeleteRequestLoading,
                            error = state.deleteRequestError?.let { stringResource(it) },
                            onAction = onAction
                        )
                    }
                }
            }
        }
    }
}


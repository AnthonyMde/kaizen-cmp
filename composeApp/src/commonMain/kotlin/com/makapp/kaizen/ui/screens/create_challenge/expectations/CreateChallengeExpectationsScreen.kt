package com.makapp.kaizen.ui.screens.create_challenge.expectations

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.makapp.kaizen.ui.components.LimitedCharTextField
import com.makapp.kaizen.ui.screens.components.BackTopAppBar
import com.makapp.kaizen.ui.screens.components.FormErrorText
import com.makapp.kaizen.ui.screens.components.LoadingButton
import com.makapp.kaizen.ui.screens.components.PlaceholderText
import com.makapp.kaizen.ui.screens.create_challenge.CreateChallengeFunnelState
import com.makapp.kaizen.ui.screens.create_challenge.CreateChallengeNavigationEvent
import com.makapp.kaizen.ui.screens.create_challenge.CreateChallengeViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun CreateChallengeExpectationsScreenRoot(
    viewModel: CreateChallengeViewModel,
    goToCommitmentStep: () -> Unit,
    navigateUp: () -> Boolean,
    navArgs: ChallengeExpectationsNavArgs,
) {
    val state by viewModel.createChallengeScreenState.collectAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(null) {
        scope.launch {
            viewModel.navigationEvents.collectLatest { event ->
                when (event) {
                    CreateChallengeNavigationEvent.GoBackHome -> navigateUp()
                    else -> {}
                }
            }
        }

        // TODO: do better by passing args directly to VM.
        if (navArgs.expectations?.isNotBlank() == true) {
            viewModel.onExpectationsAction(
                CreateChallengeExpectationsAction.OnExpectationsValueChange(navArgs.expectations)
            )
        }
    }

    CreateChallengeExpectationsScreen(
        state = state,
        navArgs = navArgs,
        onAction = { action ->
            when (action) {
                CreateChallengeExpectationsAction.GoToCommitmentStep -> goToCommitmentStep()
                CreateChallengeExpectationsAction.NavigateUp -> navigateUp()
                else -> viewModel.onExpectationsAction(action)
            }
        }
    )
}

@Composable
fun CreateChallengeExpectationsScreen(
    state: CreateChallengeFunnelState,
    onAction: (CreateChallengeExpectationsAction) -> Unit,
    navArgs: ChallengeExpectationsNavArgs
) {
    val keyboard = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            BackTopAppBar(
                title = "Expectations",
                onNavigateUp = {
                    onAction(CreateChallengeExpectationsAction.NavigateUp)
                },
                backDescription = "Go back to account.",
            )
        },
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .imePadding()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxHeight()
                    .verticalScroll(scrollState)
                    .padding(24.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.tertiaryContainer)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Kaizen is about growth, not just goals \uD83E\uDDD8\u200D♂\uFE0F Where do you want to be in 365 days? \nWrite it down - it’s your first step forward! \uD83D\uDE80",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Commitment Field.
                LimitedCharTextField(
                    onValueChange = { expectations ->
                        onAction(
                            CreateChallengeExpectationsAction.OnExpectationsValueChange(expectations)
                        )
                    },
                    value = state.expectationsInputValue,
                    maxCharAllowed = CreateChallengeViewModel.MAX_CHALLENGE_EXPECTATIONS_LENGTH,
                    textError = null,
                    shape = RoundedCornerShape(16.dp),
                    placeholder = {
                        PlaceholderText("Example (reading): \"I want to rediscover my love for reading and make it part of my daily life again.\"")
                    },
                    keyboardOptions = KeyboardOptions().copy(
                        capitalization = KeyboardCapitalization.Sentences,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.weight(1f))

                state.expectationUpdateError?.let { message ->
                    FormErrorText(
                        message,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                LoadingButton(
                    onClick = {
                        if (navArgs.editing && navArgs.challengeId != null)
                            done(
                                keyboard,
                                onAction,
                                challengeId = navArgs.challengeId
                            )
                        else
                            goNext(keyboard, onAction)
                    },
                    enabled = !state.isExpectationUpdateLoading,
                    isLoading = state.isExpectationUpdateLoading,
                    label = if (navArgs.editing) "Update" else "Next",
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
}

private fun goNext(
    keyboard: SoftwareKeyboardController?,
    onAction: (CreateChallengeExpectationsAction) -> Unit,
) {
    keyboard?.hide()
    onAction(CreateChallengeExpectationsAction.GoToCommitmentStep)
}

private fun done(
    keyboard: SoftwareKeyboardController?,
    onAction: (CreateChallengeExpectationsAction) -> Unit,
    challengeId: String
) {
    keyboard?.hide()
    onAction(
        CreateChallengeExpectationsAction.OnUpdateExpectations(challengeId)
    )
}

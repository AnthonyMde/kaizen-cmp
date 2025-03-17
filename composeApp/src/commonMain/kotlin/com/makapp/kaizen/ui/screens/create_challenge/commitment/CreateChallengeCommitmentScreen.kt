package com.makapp.kaizen.ui.screens.create_challenge.commitment

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
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
import kaizen.composeapp.generated.resources.Res
import kaizen.composeapp.generated.resources.commitment_screen_commitment_input_placeholder
import kaizen.composeapp.generated.resources.commitment_screen_done_button
import kaizen.composeapp.generated.resources.commitment_screen_info_text
import kaizen.composeapp.generated.resources.commitment_screen_title
import kaizen.composeapp.generated.resources.commitment_screen_update_button
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@Composable
fun CreateChallengeCommitmentScreenRoot(
    viewModel: CreateChallengeViewModel,
    navigateUp: () -> Unit,
    goHome: () -> Unit,
    navArgs: ChallengeCommitmentNavArgs
) {
    val state by viewModel.createChallengeScreenState.collectAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(null) {
        scope.launch {
            viewModel.navigationEvents.collectLatest { event ->
                when (event) {
                    CreateChallengeNavigationEvent.GoBackHome -> goHome()
                    CreateChallengeNavigationEvent.NavigateUp -> navigateUp()
                    else -> {}
                }
            }
        }

        // TODO: do better by passing args directly to VM.
        if (navArgs.commitment?.isNotBlank() == true) {
            viewModel.onCommitmentAction(
                CreateChallengeCommitmentAction.OnCommitmentInputValueChanged(
                    navArgs.commitment
                )
            )
        }
    }

    CreateChallengeCommitmentScreen(
        state = state,
        navArgs = navArgs,
        onAction = { action ->
            when (action) {
                CreateChallengeCommitmentAction.OnNavigateUp -> {
                    navigateUp()
                }

                else -> viewModel.onCommitmentAction(action)
            }
        }
    )
}

@Composable
fun CreateChallengeCommitmentScreen(
    state: CreateChallengeFunnelState,
    navArgs: ChallengeCommitmentNavArgs,
    onAction: (CreateChallengeCommitmentAction) -> Unit,
) {
    val keyboard = LocalSoftwareKeyboardController.current
    val focus = LocalFocusManager.current
    val scroll = rememberScrollState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            BackTopAppBar(
                title = stringResource(Res.string.commitment_screen_title),
                onNavigateUp = {
                    onAction(CreateChallengeCommitmentAction.OnNavigateUp)
                },
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(null) {
                    detectTapGestures(
                        onTap = { focus.clearFocus() }
                    )
                }
                .padding(innerPadding)
                .imePadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .verticalScroll(scroll)
                    .padding(horizontal = 24.dp)
                    .padding(top = 8.dp, bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.tertiaryContainer)
                        .padding(16.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.commitment_screen_info_text),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Commitment Field.
                LimitedCharTextField(
                    onValueChange = { commitment ->
                        onAction(
                            CreateChallengeCommitmentAction.OnCommitmentInputValueChanged(
                                commitment
                            )
                        )
                    },
                    value = state.commitmentInputValue,
                    maxCharAllowed = CreateChallengeViewModel.MAX_CHALLENGE_COMMITMENT_LENGTH,
                    textError = null,
                    shape = RoundedCornerShape(16.dp),
                    placeholder = {
                        PlaceholderText(stringResource(Res.string.commitment_screen_commitment_input_placeholder))
                    },
                    keyboardOptions = KeyboardOptions().copy(
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.weight(1f))

                state.formSubmissionError?.let { message ->
                    FormErrorText(
                        message = stringResource(message),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                LoadingButton(
                    onClick = {
                        keyboard?.hide()
                        submit(
                            onAction,
                            navArgs.editing,
                            navArgs.challengeId
                        )
                    },
                    enabled = !state.isFormSubmissionLoading,
                    isLoading = state.isFormSubmissionLoading,
                    label = if (navArgs.editing)
                        stringResource(Res.string.commitment_screen_update_button)
                    else
                        stringResource(Res.string.commitment_screen_done_button),
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
}

private fun submit(
    onAction: (CreateChallengeCommitmentAction) -> Unit,
    editing: Boolean,
    challengeId: String?
) {
    if (editing && challengeId != null) {
        onAction(
            CreateChallengeCommitmentAction.OnUpdateCommitment(
                challengeId
            )
        )
    } else if (!editing) {
        onAction(CreateChallengeCommitmentAction.OnFormSubmit)
    }
}

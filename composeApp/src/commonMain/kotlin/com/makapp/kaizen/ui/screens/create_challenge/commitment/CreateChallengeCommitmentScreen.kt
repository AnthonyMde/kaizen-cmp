package com.makapp.kaizen.ui.screens.create_challenge.commitment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
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

@Composable
fun CreateChallengeCommitmentScreenRoot(
    viewModel: CreateChallengeViewModel,
    navigateUp: () -> Unit,
    goHome: () -> Unit,
    navArgs: ChallengeCommitmentNavArgs
) {
    val state by viewModel.createChallengeScreenState.collectAsState()

    LaunchedEffect(null) {
        viewModel.navigationEvents.collectLatest { event ->
            when (event) {
                CreateChallengeNavigationEvent.GoBackHome -> goHome()
                else -> {}
            }
        }
    }

    CreateChallengeCommitmentScreen(
        state = state,
        editing = navArgs.editing,
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
    onAction: (CreateChallengeCommitmentAction) -> Unit,
    editing: Boolean
) {
    val keyboard = LocalSoftwareKeyboardController.current

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            BackTopAppBar(
                title = "Commitment",
                onNavigateUp = {
                    onAction(CreateChallengeCommitmentAction.OnNavigateUp)
                },
                backDescription = "Go back to account.",
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(24.dp),
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
                    text = "Kaizen is a 365-day adventure \uD83C\uDF34 \nSet a realistic daily minimum - doable even on your worst days. \nTiny sparks ignite great fires! \uD83D\uDD25",
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
                    PlaceholderText("Example (reading): \"I must read at least one page.\"")
                },
                keyboardOptions = KeyboardOptions().copy(
                    imeAction = ImeAction.Done,
                    capitalization = KeyboardCapitalization.Sentences
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        onDone(keyboard, onAction)
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            state.formSubmissionError?.let { message ->
                FormErrorText(
                    message,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            }

            LoadingButton(
                onClick = {
                    onDone(keyboard, onAction)
                },
                enabled = !state.isFormSubmissionLoading,
                isLoading = state.isFormSubmissionLoading,
                label = "Done",
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

private fun onDone(
    keyboard: SoftwareKeyboardController?,
    onAction: (CreateChallengeCommitmentAction) -> Unit
) {
    keyboard?.hide()
    onAction(CreateChallengeCommitmentAction.OnFormSubmit)
}
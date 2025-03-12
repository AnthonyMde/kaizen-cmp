package com.makapp.kaizen.ui.screens.create_challenge.infos

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
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
fun CreateChallengeInfosScreenRoot(
    viewModel: CreateChallengeViewModel,
    navigateUp: () -> Unit,
    goHome: () -> Unit,
    goToCommitmentStep: () -> Unit
) {
    val state by viewModel.createChallengeScreenState.collectAsState()
    val scope = rememberCoroutineScope()

    scope.launch {
        viewModel.navigationEvents.collectLatest { event ->
            when (event) {
                CreateChallengeNavigationEvent.GoHome -> goHome()
                CreateChallengeNavigationEvent.GoToCommitmentStep -> goToCommitmentStep()
            }
        }
    }

    CreateChallengeInfos(
        state = state,
        onAction = { action ->
            when (action) {
                CreateChallengeInfosAction.OnNavigateUp -> navigateUp()
                else -> viewModel.onInfosAction(action)
            }
        }
    )
}

@Composable
fun CreateChallengeInfos(
    state: CreateChallengeFunnelState,
    onAction: (CreateChallengeInfosAction) -> Unit
) {
    val keyboard = LocalSoftwareKeyboardController.current

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            BackTopAppBar(
                title = "General infos",
                onNavigateUp = {
                    onAction(CreateChallengeInfosAction.OnNavigateUp)
                },
                backDescription = "Go back to account.",
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp)
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title field.
            LimitedCharTextField(
                onValueChange = { name ->
                    onAction(CreateChallengeInfosAction.OnNameInputValueChanged(name))
                },
                value = state.challengeNameInputValue,
                maxCharAllowed = CreateChallengeViewModel.MAX_CHALLENGE_TITLE_LENGTH,
                textError = state.challengeNameInputError,
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                label = {
                    Text("Title")
                },
                placeholder = {
                    PlaceholderText("Reading")
                },
                isError = state.challengeNameInputError != null,
                keyboardOptions = KeyboardOptions().copy(
                    imeAction = ImeAction.Next,
                    capitalization = KeyboardCapitalization.Sentences
                ),
                modifier = Modifier
                    .fillMaxWidth()
            )
            // Number of errors field.
            OutlinedTextField(
                onValueChange = { numberOfErrors ->
                    onAction(
                        CreateChallengeInfosAction.OnNumberOfErrorsInputValueChanged(
                            numberOfErrors
                        )
                    )
                },
                value = state.numberOfErrorsInputValue,
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                label = {
                    Text("Number of errors")
                },
                placeholder = {
                    PlaceholderText("10")
                },
                isError = state.numberOfErrorsInputError != null,
                keyboardOptions = KeyboardOptions().copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        goNext(keyboard, onAction)
                    },
                ),
                modifier = Modifier
                    .fillMaxWidth()
            )
            state.numberOfErrorsInputError?.let { message ->
                FormErrorText(message, modifier = Modifier.padding(start = 8.dp, end = 8.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            Spacer(modifier = Modifier.weight(1f))

            state.formSubmissionError?.let { message ->
                FormErrorText(
                    message,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            }
            Button(
                onClick = {
                    goNext(keyboard, onAction)
                },
                enabled = state.challengeNameInputValue.isNotBlank() && state.numberOfErrorsInputValue.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text("Next")
            }
        }
    }
}

private fun goNext(
    keyboard: SoftwareKeyboardController?,
    onAction: (CreateChallengeInfosAction) -> Unit
) {
    keyboard?.hide()
    onAction(CreateChallengeInfosAction.GoToCommitmentStep)
}

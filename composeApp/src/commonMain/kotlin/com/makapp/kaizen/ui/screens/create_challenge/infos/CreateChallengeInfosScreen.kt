package com.makapp.kaizen.ui.screens.create_challenge.infos

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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.makapp.kaizen.ui.components.LimitedCharTextField
import com.makapp.kaizen.ui.screens.components.BackTopAppBar
import com.makapp.kaizen.ui.screens.components.FormErrorText
import com.makapp.kaizen.ui.screens.components.PlaceholderText
import com.makapp.kaizen.ui.screens.create_challenge.CreateChallengeFunnelState
import com.makapp.kaizen.ui.screens.create_challenge.CreateChallengeNavigationEvent
import com.makapp.kaizen.ui.screens.create_challenge.CreateChallengeViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CreateChallengeInfosScreenRoot(
    viewModel: CreateChallengeViewModel,
    navigateUp: () -> Unit,
    goToExpectationsStep: () -> Unit
) {
    val state by viewModel.createChallengeScreenState.collectAsState()

    LaunchedEffect(null) {
        viewModel.navigationEvents.collectLatest { event ->
            when (event) {
                CreateChallengeNavigationEvent.GoToExpectationsStep -> goToExpectationsStep()
                else -> {}
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
    val focus = LocalFocusManager.current
    val scroll = rememberScrollState()

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
                        Text("Number of lives")
                    },
                    placeholder = {
                        PlaceholderText("10")
                    },
                    supportingText = {
                        Text("How many times you can fail this challenge.")
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
}

private fun goNext(
    keyboard: SoftwareKeyboardController?,
    onAction: (CreateChallengeInfosAction) -> Unit
) {
    keyboard?.hide()
    onAction(CreateChallengeInfosAction.GoToCommitmentStep)
}

package org.towny.kaizen.ui.screens.create_challenge

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import org.towny.kaizen.ui.screens.components.BackTopAppBar
import org.towny.kaizen.ui.screens.components.FormErrorText

@Composable
fun CreateChallengeScreenRoot(
    viewModel: CreateChallengeViewModel = koinViewModel(),
    navigateUp: () -> Unit
) {
    val state by viewModel.createChallengeScreenState.collectAsState()
    CreateChallengeScreen(
        state = state,
        onAction = { action ->
            when (action) {
                CreateChallengeAction.OnNavigateUp -> navigateUp()
                else -> viewModel.onAction(action)
            }
        }
    )
}

@Composable
fun CreateChallengeScreen(
    state: CreateChallengeScreenState,
    onAction: (CreateChallengeAction) -> Unit
) {
    Scaffold(
        topBar = {
            BackTopAppBar(
                title = "Create challenge",
                onNavigateUp = {
                    onAction(CreateChallengeAction.OnNavigateUp)
                },
                backDescription = "Go back to account."
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp)
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                OutlinedTextField(
                    onValueChange = { name ->
                        onAction(CreateChallengeAction.OnNameInputValueChanged(name))
                    },
                    value = state.challengeNameInputValue,
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    label = {
                        Text("Title")
                    },
                    placeholder = {
                        Text("Reading")
                    },
                    isError = state.challengeNameInputError != null,
                    keyboardOptions = KeyboardOptions().copy(imeAction = ImeAction.Next),
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Row(modifier = Modifier.fillMaxWidth()) {
                    FormErrorText(
                        state.challengeNameInputError ?: "",
                        modifier = Modifier
                            .padding(
                                start = 8.dp, end = 8.dp,
                                top = if (state.challengeNameInputError != null) 4.dp else 0.dp,
                                bottom = if (state.challengeNameInputError != null) 4.dp else 0.dp
                            )
                            .weight(1f)
                    )

                    Text(
                        "${state.challengeNameInputValue.length}/${CreateChallengeViewModel.MAX_CHALLENGE_TITLE_LENGTH}",
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier
                            .padding(end = 8.dp)
                    )
                }
            }
            OutlinedTextField(
                onValueChange = { numberOfErrors ->
                    onAction(CreateChallengeAction.OnNumberOfErrorsInputValueChanged(numberOfErrors))
                },
                value = state.numberOfErrorsInputValue,
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                label = {
                    Text("Number of errors")
                },
                placeholder = {
                    Text("10")
                },
                isError = state.numberOfErrorsInputError != null,
                keyboardOptions = KeyboardOptions().copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier
                    .fillMaxWidth()
            )
            state.numberOfErrorsInputError?.let { message ->
                FormErrorText(message, modifier = Modifier.padding(start = 8.dp, end = 8.dp))
            }

            Spacer(modifier = Modifier.weight(1f))

            state.formSubmissionError?.let { message ->
                FormErrorText(
                    message,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            }
            FilledIconButton(
                onClick = {
                    onAction(CreateChallengeAction.OnCreateChallengeFormSubmit)
                },
                enabled = !state.isFormSubmissionLoading,
                content = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Create")
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp,
                            modifier = Modifier
                                .size(20.dp)
                                .alpha(if (state.isFormSubmissionLoading) 1f else 0f)
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}
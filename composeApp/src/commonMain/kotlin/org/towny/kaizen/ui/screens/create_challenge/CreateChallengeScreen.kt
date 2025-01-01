package org.towny.kaizen.ui.screens.create_challenge

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ElevatedButton
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import org.towny.kaizen.ui.screens.components.BackTopAppBar

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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                OutlinedTextField(
                    onValueChange = { title ->
                        onAction(CreateChallengeAction.OnTitleInputValueChanged(title))
                    },
                    value = state.challengeTitleInputValue,
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    label = {
                        Text("Title")
                    },
                    placeholder = {
                        Text("Reading")
                    },
                    keyboardOptions = KeyboardOptions().copy(imeAction = ImeAction.Next),
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Text(
                    "${state.challengeTitleInputValue.length}/${CreateChallengeViewModel.MAX_CHALLENGE_TITLE_LENGTH}",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(end = 8.dp)
                )
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
                keyboardOptions = KeyboardOptions().copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            FilledIconButton(
                onClick = {
                    // TODO: Submit form
                },
                content = {
                    Text("Create")
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}
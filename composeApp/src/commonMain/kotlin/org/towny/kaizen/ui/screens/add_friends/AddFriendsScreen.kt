package org.towny.kaizen.ui.screens.add_friends

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import org.towny.kaizen.ui.screens.components.BackTopAppBar

@Composable
fun AddFriendsScreenRoot(
    viewModel: AddFriendsViewModel = koinViewModel(),
    popToAccount: () -> Unit
) {
    val state by viewModel.addFriendsState.collectAsState()

    AddFriendsScreen(
        state = state,
        onAction = { action ->
            when (action) {
                AddFriendsAction.OnNavigateUp -> popToAccount()
                else -> viewModel.onAction(action)
            }
        }
    )
}

@Composable
fun AddFriendsScreen(
    state: AddFriendsState,
    onAction: (AddFriendsAction) -> Unit
) {
    Scaffold(
        topBar = {
            BackTopAppBar(
                title = "Add friends",
                onNavigateUp = { onAction(AddFriendsAction.OnNavigateUp) },
                backDescription = "Go back to account."
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            TextField(
                value = state.friendUsernameInputValue,
                onValueChange = { text ->
                    onAction(AddFriendsAction.OnFriendUsernameInputChanged(text))
                },
                placeholder = { Text("Enter your friend username") },
                textStyle = MaterialTheme.typography.bodyLarge,
                singleLine = true,
                isError = state.friendUsernameInputError != null,
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = {
                    onAction(AddFriendsAction.OnAddFriendFormSubmit)
                },
                content = {
                    Text("Submit")
                },
                enabled = !state.isFormSubmissionLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            )
        }
    }
}
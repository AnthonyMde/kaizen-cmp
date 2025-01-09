package org.towny.kaizen.ui.screens.my_friends

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import org.towny.kaizen.ui.screens.components.BackTopAppBar
import org.towny.kaizen.ui.screens.my_friends.components.ReceivedFriendRequestView
import org.towny.kaizen.ui.screens.my_friends.components.SentFriendRequestView

@Composable
fun MyFriendsScreenRoot(
    viewModel: MyFriendsViewModel = koinViewModel(),
    popToAccount: () -> Unit
) {
    val state by viewModel.myFriendsState.collectAsState()

    MyFriendsScreen(
        state = state,
        onAction = { action ->
            when (action) {
                MyFriendsAction.OnNavigateUp -> popToAccount()
                else -> viewModel.onAction(action)
            }
        }
    )
}

@Composable
fun MyFriendsScreen(
    state: MyFriendsState,
    onAction: (MyFriendsAction) -> Unit
) {
    Scaffold(
        topBar = {
            BackTopAppBar(
                title = "My friends",
                onNavigateUp = { onAction(MyFriendsAction.OnNavigateUp) },
                backDescription = "Go back to account."
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = state.friendUsernameInputValue,
                onValueChange = { text ->
                    onAction(MyFriendsAction.OnFriendUsernameInputChanged(text))
                },
                label = { Text("Add a friend") },
                placeholder = { Text("Friend's ID") },
                supportingText = { Text(state.friendUsernameInputError ?: "") },
                textStyle = MaterialTheme.typography.bodyLarge,
                singleLine = true,
                isError = state.friendUsernameInputError != null,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
            )

            if (state.pendingSentRequests.isNotEmpty()) {
                Text(
                    "Pending requests (${state.pendingSentRequests.size})",
                    modifier = Modifier
                        .fillMaxSize(),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface
                )
                HorizontalDivider(
                    modifier = Modifier.width(120.dp).padding(top = 8.dp),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    state.pendingSentRequests.forEach { request ->
                        SentFriendRequestView(request = request)
                    }

                    HorizontalDivider(modifier = Modifier.width(120.dp))

                    state.pendingReceivedRequests.forEach { request ->
                        ReceivedFriendRequestView(request = request)
                    }
                }
            }
        }
    }
}
package org.towny.kaizen.ui.screens.my_friends

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import org.koin.compose.viewmodel.koinViewModel
import org.towny.kaizen.ui.screens.components.BackTopAppBar
import org.towny.kaizen.ui.screens.my_friends.components.FriendPreview
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
        ) {
            OutlinedTextField(
                value = state.friendUsernameInputValue,
                onValueChange = { text ->
                    onAction(MyFriendsAction.OnFriendUsernameInputChanged(text))
                },
                label = { Text("Find a friend") },
                placeholder = { Text("Friend's username") },
                supportingText = if (state.friendPreview == null) {
                    { Text(state.friendUsernameInputError ?: "Username is case-sensitive.") }
                } else {
                    null
                },
                textStyle = MaterialTheme.typography.bodyLarge,
                singleLine = true,
                isError = state.friendUsernameInputError != null,
                shape = RoundedCornerShape(16.dp),
                trailingIcon = {
                    getTrailingIcon(
                        username = state.friendUsernameInputValue,
                        onAction = onAction,
                        isLoading = state.isFriendPreviewLoading
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = { onAction(MyFriendsAction.OnSearchFriendProfile) }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .zIndex(2f)
            )

            AnimatedVisibility(
                visible = state.friendPreview != null,
                enter = slideInVertically(
                    animationSpec = tween(300),
                ),
                modifier = Modifier.offset(y = (-16).dp)
            ) {
                state.friendPreview?.let { friend ->
                    FriendPreview(
                        friend = friend,
                        onAction = onAction
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

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

@Composable
fun getTrailingIcon(
    username: String,
    onAction: (MyFriendsAction) -> Unit,
    isLoading: Boolean = false
) {
    if (isLoading)
        return CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp)

    if (username.isEmpty()) {
        return
    }

    return IconButton(
        onClick = {
            onAction(MyFriendsAction.OnSearchFriendProfile)
        },
        content = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search friend by name",
                tint = MaterialTheme.colorScheme.primary
            )
        },
    )
}

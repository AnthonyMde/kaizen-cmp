package com.makapp.kaizen.ui.screens.my_friends

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.makapp.kaizen.ui.screens.components.BackTopAppBar
import com.makapp.kaizen.ui.screens.my_friends.components.FriendPreview
import com.makapp.kaizen.ui.screens.my_friends.components.FriendRequestsEmptyView
import com.makapp.kaizen.ui.screens.my_friends.components.FriendRowView
import com.makapp.kaizen.ui.screens.my_friends.components.FriendsEmptyView
import com.makapp.kaizen.ui.screens.my_friends.components.PendingRequestsView
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MyFriendsScreenRoot(
    viewModel: MyFriendsViewModel = koinViewModel(),
    popToAccount: () -> Unit
) {
    val state by viewModel.myFriendsState.collectAsState(MyFriendsState())
    val keyboard = LocalSoftwareKeyboardController.current

    MyFriendsScreen(
        state = state,
        onAction = { action ->
            when (action) {
                MyFriendsAction.OnNavigateUp -> popToAccount()
                MyFriendsAction.OnFriendRequestSubmit -> {
                    keyboard?.hide()
                    viewModel.onAction(action)
                }

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
                title = "Friends",
                onNavigateUp = { onAction(MyFriendsAction.OnNavigateUp) },
                backDescription = "Go back to account."
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { innerPadding ->
        val focusManager = LocalFocusManager.current

        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .pointerInput(null) {
                    detectTapGestures(
                        onTap = {
                            focusManager.clearFocus()
                        }
                    )
                }
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .padding(top = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            OutlinedTextField(
                value = state.friendUsernameInputValue,
                onValueChange = { text ->
                    onAction(MyFriendsAction.OnFriendUsernameInputChanged(text))
                },
                label = { Text("Find a friend") },
                placeholder = { Text("Friend's username") },
                supportingText = if (state.friendPreview == null && state.friendUsernameInputError != null) {
                    { Text(state.friendUsernameInputError) }
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

            @Suppress("SENSELESS_COMPARISON")
            val visible = state.friendPreview != null
            AnimatedVisibility(
                visible = visible,
                enter = slideInVertically(
                    animationSpec = tween(300),
                ),
                modifier = Modifier.offset(y = (-16).dp)
            ) {
                @Suppress("UNNECESSARY_SAFE_CALL")
                state.friendPreview?.let { friend ->
                    FriendPreview(
                        friend = friend,
                        isLoading = state.isSendFriendRequestLoading,
                        onAction = onAction
                    )
                }
            }

            if (state.totalRequests > 0) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Friend requests (${state.totalRequests})",
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                PendingRequestsView(
                    sentRequests = state.pendingSentRequests,
                    receivedRequests = state.pendingReceivedRequests,
                    requestIdsCurrentlyUpdated = state.requestIdsCurrentlyUpdated,
                    onAction = onAction
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Friends",
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (state.friendPreviews.isEmpty()) {
                FriendsEmptyView()
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(bottom = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(count = state.friendPreviews.size, itemContent = { index ->
                        FriendRowView(
                            friend = state.friendPreviews[index]
                        )
                    })
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

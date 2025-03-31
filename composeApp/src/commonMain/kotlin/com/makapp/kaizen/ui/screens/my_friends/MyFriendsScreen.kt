package com.makapp.kaizen.ui.screens.my_friends

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.makapp.kaizen.ui.components.BackTopAppBar
import com.makapp.kaizen.ui.screens.my_friends.components.FriendRowView
import com.makapp.kaizen.ui.screens.my_friends.components.FriendSearchView
import com.makapp.kaizen.ui.screens.my_friends.components.FriendsEmptyView
import com.makapp.kaizen.ui.screens.my_friends.components.PendingRequestsView
import kaizen.composeapp.generated.resources.Res
import kaizen.composeapp.generated.resources.friends_screen_back_description
import kaizen.composeapp.generated.resources.friends_screen_friends_requests_section_title
import kaizen.composeapp.generated.resources.friends_screen_friends_section_title
import kaizen.composeapp.generated.resources.friends_screen_title
import kaizen.composeapp.generated.resources.friends_search_button_description
import org.jetbrains.compose.resources.stringResource
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
                title = stringResource(Res.string.friends_screen_title),
                onNavigateUp = { onAction(MyFriendsAction.OnNavigateUp) },
                backDescription = stringResource(Res.string.friends_screen_back_description),
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
            FriendSearchView(
                usernameInputValue = state.friendUsernameInputValue,
                usernameInputError = state.friendUsernameInputError?.let { stringResource(it) },
                friendSearchPreview = state.friendSearchPreview,
                isSearchLoading = state.isFriendPreviewLoading,
                isSendRequestLoading = state.isSendFriendRequestLoading,
                onAction = onAction
            )

            if (state.totalRequests > 0) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(
                        Res.string.friends_screen_friends_requests_section_title,
                        state.totalRequests
                    ),
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                PendingRequestsView(
                    sentRequests = state.pendingSentRequests,
                    receivedRequests = state.pendingReceivedRequests,
                    requestIdsCurrentlyUpdated = state.requestIdsUnderUpdate,
                    onAction = onAction
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(Res.string.friends_screen_friends_section_title),
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
                        val friend = state.friendPreviews[index]
                        val isLoading = state.friendIdsUnderUpdate.contains(friend.id)
                        FriendRowView(
                            friend = friend,
                            isLoading = isLoading,
                            onAction = onAction
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
                contentDescription = stringResource(Res.string.friends_search_button_description),
                tint = MaterialTheme.colorScheme.primary
            )
        },
    )
}

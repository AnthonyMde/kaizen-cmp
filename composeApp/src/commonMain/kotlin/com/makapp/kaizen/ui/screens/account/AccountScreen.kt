package com.makapp.kaizen.ui.screens.account

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kaizen.composeapp.generated.resources.Res
import kaizen.composeapp.generated.resources.landscape_icon
import kaizen.composeapp.generated.resources.logout_icon
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import com.makapp.kaizen.ui.resources.avatars
import com.makapp.kaizen.ui.screens.account.components.AccountRowView
import com.makapp.kaizen.ui.screens.components.BackTopAppBar
import com.makapp.kaizen.ui.screens.components.ConfirmationModal
import com.makapp.kaizen.ui.screens.components.ConfirmationModalType


@Composable
fun AccountScreenRoot(
    popToHome: () -> Unit,
    popToLogin: () -> Unit,
    goToMyFriends: () -> Unit,
    goToCreateChallenge: () -> Unit,
    viewModel: AccountViewModel = koinViewModel()
) {
    val scope = rememberCoroutineScope()
    val state by viewModel.accountScreenState.collectAsState(AccountScreenState())

    LaunchedEffect(true) {
        scope.launch {
            viewModel.accountEvents.collectLatest { event ->
                when (event) {
                    AccountEvent.PopToLogin -> popToLogin()
                }
            }
        }
    }

    AccountScreen(
        state = state,
        onAction = { action ->
            when (action) {
                AccountAction.OnNavigateUp -> popToHome()
                AccountAction.GoToMyFriends -> goToMyFriends()
                AccountAction.GoToCreateChallenge -> goToCreateChallenge()
                else -> viewModel.onAction(action)
            }
        })
}

@Composable
fun AccountScreen(
    state: AccountScreenState,
    onAction: (AccountAction) -> Unit
) {
    if (state.user == null) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Scaffold(
            topBar = {
                BackTopAppBar(
                    title = "Account",
                    onNavigateUp = { onAction(AccountAction.OnNavigateUp) },
                    backDescription = "Go back home.",
                    actions = {
                        IconButton(
                            onClick = { onAction(AccountAction.OnLogoutClicked) },
                            enabled = !state.isLogoutLoading,
                            content = {
                                Icon(
                                    painter = painterResource(Res.drawable.logout_icon),
                                    contentDescription = "Logout.",
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                            }
                        )
                    }
                )
            },
            containerColor = MaterialTheme.colorScheme.surface
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 24.dp)
                    .padding(top = 24.dp)
            ) {
                if (state.showLogoutConfirmationModal) {
                    LogoutConfirmationModal(onAction, state.isLogoutLoading)
                }
                if (state.showDeleteUserAccountConfirmationModal) {
                    DeleteAccountConfirmationModal(
                        onAction = onAction,
                        isLoading = state.isDeleteUserAccountLoading,
                        error = state.deleteUserAccountError
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(avatars[state.user.profilePictureIndex].drawable),
                        contentDescription = "Profile picture",
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            state.user.name,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
                        )
                        Text(
                            "Golden Kaizen", // TODO: create kaizen status
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

                HorizontalDivider(Modifier.fillMaxWidth().padding(24.dp))

                AccountRowView(
                    onAction = {
                        onAction(AccountAction.GoToCreateChallenge)
                    },
                    title = "Create challenge",
                    icon = painterResource(Res.drawable.landscape_icon),
                    description = "Create a new challenge.",
                )

                AccountRowView(
                    onAction = {
                        onAction(AccountAction.GoToMyFriends)
                    },
                    title = "My friends",
                    icon = rememberVectorPainter(Icons.Filled.Face),
                    description = "My friends.",
                    modifier = Modifier.padding(top = 8.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                TextButton(
                    onClick = {
                        onAction(AccountAction.OnDeleteAccountClicked)
                    },
                    content = {
                        Text(
                            "Delete my account",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier
                        )
                    },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun LogoutConfirmationModal(
    onAction: (AccountAction) -> Unit,
    isLoading: Boolean
) {
    ConfirmationModal(
        title = "Leaving?",
        subtitle = "Please confirm to logout",
        confirmationButtonText = "Logout",
        onConfirmed = {
            onAction(AccountAction.OnLogoutConfirmed)
        },
        onDismissed = {
            onAction(AccountAction.OnLogoutDismissed)
        },
        isConfirmationLoading = isLoading
    )
}

@Composable
private fun DeleteAccountConfirmationModal(
    onAction: (AccountAction) -> Unit,
    isLoading: Boolean,
    error: String?
) {
    ConfirmationModal(
        title = "Be careful",
        subtitle = "You are about to delete your account. This operation cannot be reverted.\n\nYou will lost all your friends, challenges and statictics.\n\nAll your data will be erased from our servers.",
        confirmationButtonText = "Delete my account (irreversible)",
        onConfirmed = {
            onAction(AccountAction.OnDeleteAccountConfirmed)
        },
        onDismissed = {
            onAction(AccountAction.OnDeleteAccountDismissed)
        },
        isConfirmationLoading = isLoading,
        type = ConfirmationModalType.DANGER,
        error = error
    )
}

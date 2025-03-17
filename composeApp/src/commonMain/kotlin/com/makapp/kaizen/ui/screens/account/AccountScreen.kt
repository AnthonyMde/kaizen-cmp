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
import com.makapp.kaizen.data.appVersion
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
import kaizen.composeapp.generated.resources.account_back_description
import kaizen.composeapp.generated.resources.account_create_challenge_row_description
import kaizen.composeapp.generated.resources.account_create_challenge_row_title
import kaizen.composeapp.generated.resources.account_delete_account_button_title
import kaizen.composeapp.generated.resources.account_delete_modal_button
import kaizen.composeapp.generated.resources.account_delete_modal_subtitle
import kaizen.composeapp.generated.resources.account_delete_modal_title
import kaizen.composeapp.generated.resources.account_friends_row_description
import kaizen.composeapp.generated.resources.account_friends_row_title
import kaizen.composeapp.generated.resources.account_logout_description
import kaizen.composeapp.generated.resources.account_logout_modal_button
import kaizen.composeapp.generated.resources.account_logout_modal_subtitle
import kaizen.composeapp.generated.resources.account_logout_modal_title
import kaizen.composeapp.generated.resources.account_profile_picture_description
import kaizen.composeapp.generated.resources.account_profile_username
import kaizen.composeapp.generated.resources.account_screen_title
import org.jetbrains.compose.resources.stringResource


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
                    title = stringResource(Res.string.account_screen_title),
                    onNavigateUp = { onAction(AccountAction.OnNavigateUp) },
                    backDescription = stringResource(Res.string.account_back_description),
                    actions = {
                        IconButton(
                            onClick = { onAction(AccountAction.OnLogoutClicked) },
                            enabled = !state.isLogoutLoading,
                            content = {
                                Icon(
                                    painter = painterResource(Res.drawable.logout_icon),
                                    contentDescription = stringResource(Res.string.account_logout_description),
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
                        contentDescription = stringResource(Res.string.account_profile_picture_description),
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
                            state.user.getUsername(),
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
                        )
                        Text(
                            stringResource(Res.string.account_profile_username, state.user.name),
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
                    title = stringResource(Res.string.account_create_challenge_row_title),
                    icon = painterResource(Res.drawable.landscape_icon),
                    description = stringResource(Res.string.account_create_challenge_row_description),
                )

                AccountRowView(
                    onAction = {
                        onAction(AccountAction.GoToMyFriends)
                    },
                    title = stringResource(Res.string.account_friends_row_title),
                    icon = rememberVectorPainter(Icons.Filled.Face),
                    description = stringResource(Res.string.account_friends_row_description),
                    modifier = Modifier.padding(top = 8.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                TextButton(
                    onClick = {
                        onAction(AccountAction.OnDeleteAccountClicked)
                    },
                    content = {
                        Text(
                            stringResource(Res.string.account_delete_account_button_title),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier
                        )
                    },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )

                Text(
                    appVersion,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally))

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
        title = stringResource(Res.string.account_logout_modal_title),
        subtitle = stringResource(Res.string.account_logout_modal_subtitle),
        confirmationButtonText = stringResource(Res.string.account_logout_modal_button),
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
        title = stringResource(Res.string.account_delete_modal_title),
        subtitle = stringResource(Res.string.account_delete_modal_subtitle),
        confirmationButtonText = stringResource(Res.string.account_delete_modal_button),
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

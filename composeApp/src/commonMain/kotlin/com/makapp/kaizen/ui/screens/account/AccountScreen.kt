package com.makapp.kaizen.ui.screens.account

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
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
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import com.makapp.kaizen.data.appVersion
import com.makapp.kaizen.ui.screens.account.components.AccountHeaderView
import com.makapp.kaizen.ui.screens.account.components.AccountLoadingView
import com.makapp.kaizen.ui.screens.account.components.AccountRowView
import com.makapp.kaizen.ui.screens.account.components.modal.DeleteAccountConfirmationModalView
import com.makapp.kaizen.ui.screens.account.components.modal.DeleteFinalConfirmationModalView
import com.makapp.kaizen.ui.screens.account.components.modal.LogoutConfirmationModalView
import com.makapp.kaizen.ui.screens.components.BackTopAppBar
import kaizen.composeapp.generated.resources.Res
import kaizen.composeapp.generated.resources.account_back_description
import kaizen.composeapp.generated.resources.account_create_challenge_row_description
import kaizen.composeapp.generated.resources.account_create_challenge_row_title
import kaizen.composeapp.generated.resources.account_delete_account_button_title
import kaizen.composeapp.generated.resources.account_friends_row_description
import kaizen.composeapp.generated.resources.account_friends_row_title
import kaizen.composeapp.generated.resources.account_logout_description
import kaizen.composeapp.generated.resources.account_screen_title
import kaizen.composeapp.generated.resources.landscape_icon
import kaizen.composeapp.generated.resources.logout_icon
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel


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
        AccountLoadingView()
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
                    LogoutConfirmationModalView(onAction, state.isLogoutLoading)
                }
                if (state.showDeleteUserAccountConfirmationModal) {
                    DeleteAccountConfirmationModalView(
                        onAction = onAction
                    )
                }
                if (state.showDeleteFinalConfirmationModal) {
                    DeleteFinalConfirmationModalView(
                        onAction = onAction,
                        isLoading = state.isDeleteUserAccountLoading,
                        error = state.deleteUserAccountError?.let { stringResource(it) }
                    )
                }

                AccountHeaderView(user = state.user)

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
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@file:OptIn(ExperimentalMaterial3Api::class)

package org.towny.kaizen.ui.screens.account

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.towny.kaizen.ui.resources.avatars
import org.towny.kaizen.ui.screens.account.components.AccountRowView
import org.towny.kaizen.ui.screens.components.BackTopAppBar


@Composable
fun AccountScreenRoot(
    popToHome: () -> Unit,
    popToLogin: () -> Unit,
    goToAddFriends: () -> Unit,
    goToCreateChallenge: () -> Unit,
    viewModel: AccountViewModel = koinViewModel()
) {
    val scope = rememberCoroutineScope()
    val state by viewModel.accountScreenState.collectAsState(AccountScreenState())

    AccountScreen(
        state = state,
        onAction = { action ->
            when (action) {
                AccountAction.OnLogout -> {
                    scope.launch {
                        viewModel.onAction(action)
                        popToLogin()
                    }
                }

                AccountAction.OnNavigateUp -> popToHome()
                AccountAction.GoToAddFriends -> goToAddFriends()
                AccountAction.GoToCreateChallenge -> goToCreateChallenge()
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
                    backDescription = "Go back home."
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
                        onAction(AccountAction.GoToAddFriends)
                    },
                    title = "Add friends",
                    icon = rememberVectorPainter(Icons.Filled.Face),
                    description = "Add a friends.",
                    modifier = Modifier.padding(top = 8.dp)
                )

                AccountRowView(
                    onAction = {
                        onAction(AccountAction.OnLogout)
                    },
                    title = "Logout",
                    icon = painterResource(Res.drawable.logout_icon),
                    description = "Logout",
                    enabled = !state.isLogoutLoading,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}
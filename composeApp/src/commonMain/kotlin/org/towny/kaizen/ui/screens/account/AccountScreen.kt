@file:OptIn(ExperimentalMaterial3Api::class)

package org.towny.kaizen.ui.screens.account

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kaizen.composeapp.generated.resources.Res
import kaizen.composeapp.generated.resources.avatar_1_x3
import kaizen.composeapp.generated.resources.hiking_icon
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
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
    val state by viewModel.accountScreenState.collectAsState()

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
    Scaffold(
        topBar = {
            BackTopAppBar(
                title = "Account",
                onNavigateUp = { onAction(AccountAction.OnNavigateUp) },
                backDescription = "Go back home."
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(Res.drawable.avatar_1_x3),
                    contentDescription = "Profile picture",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        "Towny", // TODO: get current username
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
                icon = painterResource(Res.drawable.hiking_icon),
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
                icon = rememberVectorPainter(Icons.AutoMirrored.Default.ExitToApp),
                description = "Logout",
                enabled = !state.isLogoutLoading,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
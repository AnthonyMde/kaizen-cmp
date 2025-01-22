package com.makapp.kaizen.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import com.makapp.kaizen.domain.models.Friend
import com.makapp.kaizen.ui.screens.home.components.CurrentUserView
import com.makapp.kaizen.ui.screens.components.ConfirmationModal
import com.makapp.kaizen.ui.screens.home.components.FriendWithChallengesView
import com.makapp.kaizen.ui.screens.home.components.FriendsEmptyView
import com.makapp.kaizen.ui.screens.home.components.Header

@Composable
fun HomeScreenRoot(
    homeViewModel: HomeViewModel = koinViewModel(),
    goToAccount: () -> Unit,
    popToLogin: () -> Unit,
    goToCreateChallenge: () -> Unit,
    goToCreateUserAccount: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val homeScreenState by homeViewModel.homeScreenState.collectAsState(HomeScreenState())

    LaunchedEffect(true) {
        scope.launch {
            homeViewModel.navigationEvents.collectLatest { event ->
                when (event) {
                    HomeNavigationEvent.PopToLogin -> popToLogin()
                    HomeNavigationEvent.GoToUserAccountCreation -> goToCreateUserAccount()
                }
            }
        }
    }

    HomeScreen(
        state = homeScreenState,
        onAction = { action ->
            when (action) {
                HomeAction.OnAccountClicked -> goToAccount()
                HomeAction.OnCreateFirstChallengeClicked -> goToCreateChallenge()
                else -> homeViewModel.onAction(action)
            }
        },
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.surface)
            .systemBarsPadding()
    )
}

@Composable
fun HomeScreen(
    state: HomeScreenState,
    onAction: (HomeAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .padding(top = 24.dp),
    ) {
        // TODO: make it a non-blocking snackbar + implement a cron to remove unverified accounts.
        if (state.userSession?.isEmailVerified == false) {
            ConfirmationModal(
                title = "Email verification",
                subtitle = "Check your emails to verify your account and enjoy Kaizen.",
                confirmationButtonText = "It's good now!",
                onConfirmed = {
                    onAction(HomeAction.OnEmailVerified)
                },
                canBeDismissed = false
            )
        }
        Header(
            onAction = onAction,
            profilePictureIndex = state.currentChallenger?.profilePictureIndex
        )
        CurrentUserView(
            user = state.currentChallenger,
            onAction = onAction,
            error = state.currentChallengerError,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .align(Alignment.CenterHorizontally)
        )
        HorizontalDivider(
            Modifier.padding(top = 24.dp),
            color = MaterialTheme.colorScheme.primary
        )

        if (state.isFriendsLoading) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .padding(top = 8.dp)
                    .padding(horizontal = 24.dp)
            )
        } else if (state.friends.isEmpty()) {
            FriendsEmptyView(modifier = Modifier.padding(top = 16.dp))
        }
        LazyColumn(modifier = Modifier.padding(top = 0.dp)) {
            items(state.friends) { friend ->
                FriendWithChallengesView(
                    modifier = Modifier.padding(top = 16.dp),
                    friend = Friend(
                        id = friend.id,
                        name = friend.name,
                        profilePictureIndex = friend.profilePictureIndex,
                        challenges = friend.challenges
                    )
                )
            }
        }
    }
}

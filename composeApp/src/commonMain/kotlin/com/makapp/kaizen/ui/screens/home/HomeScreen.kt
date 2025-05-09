package com.makapp.kaizen.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.makapp.kaizen.ui.screens.challenge_details.ChallengeDetailsNavArgs
import com.makapp.kaizen.ui.screens.home.components.CurrentUserView
import com.makapp.kaizen.ui.screens.home.components.FriendWithChallengesView
import com.makapp.kaizen.ui.screens.home.components.FriendsEmptyView
import com.makapp.kaizen.ui.screens.home.components.Header
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreenRoot(
    homeViewModel: HomeViewModel = koinViewModel(),
    goToAccount: () -> Unit,
    popToLogin: () -> Unit,
    goToCreateChallenge: () -> Unit,
    goToCreateUserAccount: () -> Unit,
    goToFriendsScreen: () -> Unit,
    goToChallengeDetails: (args: ChallengeDetailsNavArgs) -> Unit
) {
    val homeScreenState by homeViewModel.homeScreenState.collectAsStateWithLifecycle(HomeScreenState())
    val homeNavigationEvent by homeViewModel.events.collectAsStateWithLifecycle(null)

    LaunchedEffect(homeNavigationEvent) {
        when (homeNavigationEvent) {
            HomeNavigationEvent.PopToLogin -> popToLogin()
            HomeNavigationEvent.GoToUserAccountCreation -> goToCreateUserAccount()
            null -> {}
        }
    }

    LifecycleEventEffect(Lifecycle.Event.ON_START) {
        homeViewModel.onAction(HomeAction.OnRefreshFriendsOnResume)
    }

    HomeScreen(
        state = homeScreenState,
        onAction = { action ->
            when (action) {
                HomeAction.OnAccountClicked -> goToAccount()
                HomeAction.OnCreateFirstChallengeClicked -> goToCreateChallenge()
                HomeAction.OnFriendEmptyViewClicked -> goToFriendsScreen()
                is HomeAction.OnChallengeClicked -> {
                    goToChallengeDetails(action.navArgs)
                }

                else -> homeViewModel.onAction(action)
            }
        },
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.surface)
            .systemBarsPadding()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeScreenState,
    onAction: (HomeAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .fillMaxSize()
            .padding(top = 24.dp),
    ) {
        Header(
            onAction = onAction,
            profilePictureIndex = state.currentChallenger?.profilePictureIndex,
            isEmailVerified = state.userSession?.isEmailVerified == false
        )
        CurrentUserView(
            user = state.currentChallenger,
            onAction = onAction,
            error = state.currentChallengerError?.let { stringResource(it) },
            isLoading = state.isCurrentChallengerLoading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 16.dp)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(24.dp))

        HorizontalDivider(
            Modifier.padding(horizontal = 24.dp),
            color = MaterialTheme.colorScheme.primary
        )

        if (state.isFriendsLoading) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .padding(top = 8.dp)
                    .padding(horizontal = 48.dp)
            )
        }

        if (state.friends.isEmpty() && !state.isFriendsLoading) {
            FriendsEmptyView(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .padding(horizontal = 24.dp)
                    .clickable { onAction(HomeAction.OnFriendEmptyViewClicked) }
            )
        } else {
            PullToRefreshBox(
                isRefreshing = state.isSwipeToRefreshing,
                onRefresh = {
                    onAction(HomeAction.OnSwipeToRefreshFriendList)
                }
            ) {
                LazyColumn(contentPadding = PaddingValues(bottom = 16.dp)) {
                    items(state.friends) { friend ->
                        FriendWithChallengesView(
                            friend = friend,
                            onAction = onAction,
                            modifier = Modifier
                                .padding(top = 16.dp)
                                .padding(horizontal = 24.dp),
                        )
                    }
                }
            }
        }
    }
}

package org.towny.kaizen.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import org.towny.kaizen.ui.screens.home.components.ChallengerView
import org.towny.kaizen.ui.screens.home.components.CurrentUserView
import org.towny.kaizen.ui.screens.home.components.Header

@Composable
fun HomeScreenRoot(
    modifier: Modifier = Modifier,
    popToLogin: () -> Unit,
    homeViewModel: HomeViewModel = koinViewModel()
) {
    val homeScreenState by homeViewModel.homeScreenState.collectAsState(HomeScreenState())

    HomeScreen(
        state = homeScreenState,
        onAction = { action ->
            if (action is HomeAction.OnLogout) {
                homeViewModel.onAction(action)
                popToLogin()
            } else
                homeViewModel.onAction(action)
        },
        modifier = modifier
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
        Header(isLogoutLoading = state.isLogoutLoading, onAction = onAction)
        state.currentChallenger?.let {
            CurrentUserView(
                user = it,
                onAction = onAction,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
        HorizontalDivider(
            Modifier.padding(top = 32.dp),
            color = MaterialTheme.colorScheme.primary
        )

        LazyColumn(modifier = Modifier.padding(top = 8.dp)) {
            items(state.otherChallengers) { challenger ->
                ChallengerView(
                    modifier = Modifier.padding(top = 16.dp),
                    user = challenger,
                    onToggleChallenge = { challengeId: String, isChecked: Boolean ->
                        onAction(
                            HomeAction.OnToggleChallenge(
                                challenger.id,
                                challengeId,
                                isChecked
                            )
                        )
                    }
                )
            }
        }
    }
}

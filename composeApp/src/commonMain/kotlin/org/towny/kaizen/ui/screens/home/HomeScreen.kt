package org.towny.kaizen.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.towny.kaizen.data.remote.UsersFirestoreDataSource
import org.towny.kaizen.data.repository.UserRepositoryImpl
import org.towny.kaizen.ui.screens.home.components.ChallengerView
import org.towny.kaizen.ui.screens.home.components.UserView
import org.towny.kaizen.utils.DateUtils

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = viewModel {
        // TODO: dependency injection
        HomeViewModel(
            UserRepositoryImpl(
                UsersFirestoreDataSource()
            )
        )
    }
) {
    val homeScreenState by homeViewModel.homeScreenState.collectAsState(HomeScreenState())

    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .padding(top = 24.dp),
    ) {
        Text(
            text = DateUtils.getTodaysDate(),
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
        )
        Text(
            text = "Day ${DateUtils.getNumberOfDaysSince()}",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.primary
        )
        homeScreenState.currentChallenger?.let {
            UserView(
                it,
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
            items(homeScreenState.otherChallengers) { challenger ->
                ChallengerView(
                    modifier = Modifier.padding(top = 16.dp),
                    user = challenger
                )
            }
        }
    }
}
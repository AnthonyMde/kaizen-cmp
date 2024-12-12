package org.towny.kaizen.ui.screens.home

import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.towny.kaizen.ui.screens.home.components.ChallengerView
import org.towny.kaizen.ui.screens.home.components.UserView
import org.towny.kaizen.utils.DateUtils

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = viewModel { HomeViewModel() }
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .padding(top = 8.dp),
    ) {
        Text(
            text = DateUtils.getTodaysDate(),
            modifier = Modifier.padding(20.dp).align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        UserView(
            homeViewModel.mockedUser,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .align(Alignment.CenterHorizontally)
        )
        HorizontalDivider(
            Modifier.padding(top = 16.dp),
            color = MaterialTheme.colorScheme.primary
        )
        LazyColumn(modifier = Modifier.padding(top = 8.dp)) {
            items(homeViewModel.mockedChallengers) { challenger ->
                ChallengerView(
                    modifier = Modifier.padding(top = 16.dp),
                    challenger = challenger
                )
            }
        }
    }
}
package com.makapp.kaizen.ui.screens.archived_kaizens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.makapp.kaizen.ui.components.BackTopAppBar
import com.makapp.kaizen.ui.screens.archived_kaizens.components.ArchivedKaizenList
import com.makapp.kaizen.ui.screens.archived_kaizens.components.ArchivedKaizensEmptyView
import kaizen.composeapp.generated.resources.Res
import kaizen.composeapp.generated.resources.archived_kaizens_back_description
import kaizen.composeapp.generated.resources.archived_kaizens_screen_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ArchivedKaizensScreenRoot(
    onNavigateUp: () -> Unit,
    goToChallengeDetails: (String) -> Unit,
    viewModel: ArchivedKaizensViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ArchivedKaizensScreen(
        uiState = uiState,
        onAction = { action ->
            when (action) {
                ArchivedKaizensAction.OnNavigateUp -> {
                    onNavigateUp()
                }

                is ArchivedKaizensAction.GoToChallengeDetails -> {
                    goToChallengeDetails(action.id)
                }
            }
        }
    )
}

@Composable
fun ArchivedKaizensScreen(
    uiState: ArchivedKaizensUiState,
    onAction: (ArchivedKaizensAction) -> Unit,
) {
    Scaffold(
        topBar = {
            BackTopAppBar(
                title = stringResource(Res.string.archived_kaizens_screen_title),
                onNavigateUp = { onAction(ArchivedKaizensAction.OnNavigateUp) },
                backDescription = stringResource(Res.string.archived_kaizens_back_description),
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = 24.dp)
        ) {
            when (uiState) {
                is ArchivedKaizensUiState.Data -> {
                    ArchivedKaizenList(
                        abandonedKaizens = uiState.abandonedKaizens,
                        failedKaizens = uiState.failedKaizens,
                        onAction = onAction,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )
                }

                ArchivedKaizensUiState.Empty -> {
                    ArchivedKaizensEmptyView(
                        modifier = Modifier.fillMaxSize()
                    )
                }

                is ArchivedKaizensUiState.Error -> {
                    // TODO
                }

                ArchivedKaizensUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}
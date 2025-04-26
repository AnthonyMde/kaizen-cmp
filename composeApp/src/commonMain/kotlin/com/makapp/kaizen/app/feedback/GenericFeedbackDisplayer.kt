package com.makapp.kaizen.app.feedback

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.koinInject

@Composable
fun GenericFeedbackDisplayer(
    appFeedbackManager: AppFeedbackManager = koinInject(),
    content: @Composable () -> Unit,
) {
    val feedback by appFeedbackManager.feedback.collectAsStateWithLifecycle(null)
    val snackHostState = remember { SnackbarHostState() }

    LaunchedEffect(feedback) {
        when (val action = feedback) {
            is FeedbackAction.Snack.Display -> {
                snackHostState.showSnackbar(
                    message = action.title,
                    duration = action.duration,
                )
            }
            is FeedbackAction.Snack.Hide -> {
                // TODO
            }
            null -> {}
        }
    }

    content()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .padding(bottom = 16.dp)
            .padding(horizontal = 8.dp)
            .imePadding(),
        contentAlignment = Alignment.BottomCenter,
    ) {
        SnackbarHost(
            hostState = snackHostState,
        ) // TODO: adding a content here will override default snackbar design.
    }
}
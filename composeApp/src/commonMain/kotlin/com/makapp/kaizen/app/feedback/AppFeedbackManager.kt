package com.makapp.kaizen.app.feedback

import androidx.compose.material3.SnackbarDuration
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class AppFeedbackManager {
    private val _feedback = Channel<FeedbackAction>()
    val feedback = _feedback.receiveAsFlow()

    suspend fun displaySnackBar(
        message: String,
        duration: SnackbarDuration = SnackbarDuration.Long,
    ) {
        val snack = FeedbackAction.Snack.Display(
            title = message,
            duration = duration
        )
        _feedback.send(snack)
    }
}
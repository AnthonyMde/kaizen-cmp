@file:OptIn(ExperimentalUuidApi::class)

package com.makapp.kaizen.app.feedback

import androidx.compose.material3.SnackbarDuration
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

sealed interface FeedbackAction {
    sealed interface Snack : FeedbackAction {
        data class Display(
            val title: String,
            val duration: SnackbarDuration = SnackbarDuration.Long,
            val id: String = Uuid.random().toString()
        ) : Snack

        data class Hide(val id: String) : Snack
    }
}
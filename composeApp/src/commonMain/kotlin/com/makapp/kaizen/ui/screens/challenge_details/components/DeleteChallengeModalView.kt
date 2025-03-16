package com.makapp.kaizen.ui.screens.challenge_details.components

import androidx.compose.runtime.Composable
import com.makapp.kaizen.ui.screens.challenge_details.ChallengeDetailsAction
import com.makapp.kaizen.ui.screens.components.ConfirmationModal
import com.makapp.kaizen.ui.screens.components.ConfirmationModalType

@Composable
fun DeleteChallengeModalView(
    challengeId: String,
    isLoading: Boolean,
    error: String?,
    onAction: (ChallengeDetailsAction) -> Unit,
) {
    ConfirmationModal(
        title = "Delete this challenge",
        subtitle = "This challenge will be permanently deleted and cannot be resumed. \n\nIt will also be removed from your archived challenges.",
        confirmationButtonText = "Delete permanently",
        onDismissed = {
            onAction(ChallengeDetailsAction.OnDeleteModalDismissed)
        },
        onConfirmed = {
            onAction(ChallengeDetailsAction.OnDeleteChallengeConfirmed(challengeId = challengeId))
        },
        isConfirmationLoading = isLoading,
        canBeDismissed = true,
        type = ConfirmationModalType.DANGER,
        error = error
    )
}

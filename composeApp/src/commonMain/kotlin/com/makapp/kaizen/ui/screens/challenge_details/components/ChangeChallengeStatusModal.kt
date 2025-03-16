package com.makapp.kaizen.ui.screens.challenge_details.components

import androidx.compose.runtime.Composable
import com.makapp.kaizen.domain.models.Challenge
import com.makapp.kaizen.ui.screens.challenge_details.ChallengeDetailsAction
import com.makapp.kaizen.ui.screens.components.ConfirmationModal
import com.makapp.kaizen.ui.screens.components.ConfirmationModalType

@Composable
fun ChangeChallengeStatusModal(
    newRequestedStatus: Challenge.Status,
    challengeId: String,
    isLoading: Boolean,
    error: String?,
    onAction: (ChallengeDetailsAction) -> Unit
) {
    ConfirmationModal(
        title = getChangeStatusModalTitle(newRequestedStatus),
        subtitle = getChangeStatusModalSubtitle(newRequestedStatus),
        confirmationButtonText = getChangeStatusModalButtonText(newRequestedStatus),
        onDismissed = {
            onAction(ChallengeDetailsAction.OnChangeStatusModalDismissed)
        },
        onConfirmed = {
            onAction(
                ChallengeDetailsAction.OnChangeStatusConfirmed(
                    challengeId = challengeId,
                    newStatus = newRequestedStatus
                )
            )
        },
        isConfirmationLoading = isLoading,
        canBeDismissed = true,
        type = getChangeStatusModalType(newRequestedStatus),
        error = error
    )
}

private fun getChangeStatusModalTitle(status: Challenge.Status): String {
    return when (status) {
        Challenge.Status.ON_GOING -> "Resume this challenge"
        Challenge.Status.PAUSED -> "Pause this challenge"
        Challenge.Status.DONE -> ""
        Challenge.Status.FAILED -> ""
        Challenge.Status.ABANDONED -> "Give up on this challenge"
    }
}

private fun getChangeStatusModalSubtitle(status: Challenge.Status): String {
    return when (status) {
        Challenge.Status.ON_GOING -> "Your progress will resume and you will have to do your challenge today."
        Challenge.Status.PAUSED -> "Your progress will be paused. You will be able to resume your challenge at anytime."
        Challenge.Status.DONE -> ""
        Challenge.Status.FAILED -> ""
        Challenge.Status.ABANDONED -> "This challenge cannot be recovered once abandoned. \nHowever, you can still view it in your profile under the Archived Challenges section."
    }
}

private fun getChangeStatusModalButtonText(status: Challenge.Status): String {
    return when (status) {
        Challenge.Status.ON_GOING -> "Resume"
        Challenge.Status.PAUSED -> "Pause"
        Challenge.Status.DONE -> ""
        Challenge.Status.FAILED -> ""
        Challenge.Status.ABANDONED -> "Give up"
    }
}

private fun getChangeStatusModalType(status: Challenge.Status): ConfirmationModalType {
    return when (status) {
        Challenge.Status.ON_GOING,
        Challenge.Status.PAUSED,
        Challenge.Status.DONE -> ConfirmationModalType.WARNING
        Challenge.Status.FAILED,
        Challenge.Status.ABANDONED -> ConfirmationModalType.DANGER
    }
}

package com.makapp.kaizen.ui.screens.challenge_details.components

import androidx.compose.runtime.Composable
import com.makapp.kaizen.domain.models.challenge.Challenge
import com.makapp.kaizen.ui.screens.challenge_details.ChallengeDetailsAction
import com.makapp.kaizen.ui.screens.components.ConfirmationModal
import com.makapp.kaizen.ui.screens.components.ConfirmationModalType
import kaizen.composeapp.generated.resources.Res
import kaizen.composeapp.generated.resources.challenge_details_change_status_modal_abandoned_button
import kaizen.composeapp.generated.resources.challenge_details_change_status_modal_abandoned_subtitle
import kaizen.composeapp.generated.resources.challenge_details_change_status_modal_abandoned_title
import kaizen.composeapp.generated.resources.challenge_details_change_status_modal_ongoing_button
import kaizen.composeapp.generated.resources.challenge_details_change_status_modal_ongoing_subtitle
import kaizen.composeapp.generated.resources.challenge_details_change_status_modal_ongoing_title
import kaizen.composeapp.generated.resources.challenge_details_change_status_modal_pause_button
import kaizen.composeapp.generated.resources.challenge_details_change_status_modal_pause_subtitle
import kaizen.composeapp.generated.resources.challenge_details_change_status_modal_pause_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun ChangeChallengeStatusModalView(
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

@Composable
private fun getChangeStatusModalTitle(status: Challenge.Status): String {
    return when (status) {
        Challenge.Status.ON_GOING -> stringResource(Res.string.challenge_details_change_status_modal_ongoing_title)
        Challenge.Status.PAUSED -> stringResource(Res.string.challenge_details_change_status_modal_pause_title)
        Challenge.Status.DONE -> ""
        Challenge.Status.FAILED -> ""
        Challenge.Status.ABANDONED -> stringResource(Res.string.challenge_details_change_status_modal_abandoned_title)
    }
}

@Composable
private fun getChangeStatusModalSubtitle(status: Challenge.Status): String {
    return when (status) {
        Challenge.Status.ON_GOING -> stringResource(Res.string.challenge_details_change_status_modal_ongoing_subtitle)
        Challenge.Status.PAUSED -> stringResource(Res.string.challenge_details_change_status_modal_pause_subtitle)
        Challenge.Status.DONE -> ""
        Challenge.Status.FAILED -> ""
        Challenge.Status.ABANDONED -> stringResource(Res.string.challenge_details_change_status_modal_abandoned_subtitle)
    }
}
@Composable
private fun getChangeStatusModalButtonText(status: Challenge.Status): String {
    return when (status) {
        Challenge.Status.ON_GOING -> stringResource(Res.string.challenge_details_change_status_modal_ongoing_button)
        Challenge.Status.PAUSED -> stringResource(Res.string.challenge_details_change_status_modal_pause_button)
        Challenge.Status.DONE -> ""
        Challenge.Status.FAILED -> ""
        Challenge.Status.ABANDONED -> stringResource(Res.string.challenge_details_change_status_modal_abandoned_button)
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

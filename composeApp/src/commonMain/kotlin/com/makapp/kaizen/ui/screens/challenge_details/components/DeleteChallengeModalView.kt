package com.makapp.kaizen.ui.screens.challenge_details.components

import androidx.compose.runtime.Composable
import com.makapp.kaizen.ui.screens.challenge_details.ChallengeDetailsAction
import com.makapp.kaizen.ui.screens.components.ConfirmationModalView
import com.makapp.kaizen.ui.screens.components.ConfirmationModalType
import kaizen.composeapp.generated.resources.Res
import kaizen.composeapp.generated.resources.challenge_details_delete_modal_button
import kaizen.composeapp.generated.resources.challenge_details_delete_modal_subtitle
import kaizen.composeapp.generated.resources.challenge_details_delete_modal_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun DeleteChallengeModalView(
    challengeId: String,
    isLoading: Boolean,
    error: String?,
    onAction: (ChallengeDetailsAction) -> Unit,
) {
    ConfirmationModalView(
        title = stringResource(Res.string.challenge_details_delete_modal_title),
        subtitle = stringResource(Res.string.challenge_details_delete_modal_subtitle),
        confirmationButtonText = stringResource(Res.string.challenge_details_delete_modal_button),
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

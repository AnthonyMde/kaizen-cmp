package com.makapp.kaizen.ui.screens.account.components.modal

import androidx.compose.runtime.Composable
import com.makapp.kaizen.ui.screens.account.AccountAction
import com.makapp.kaizen.ui.screens.components.ConfirmationModalType
import com.makapp.kaizen.ui.screens.components.ConfirmationModalView
import kaizen.composeapp.generated.resources.Res
import kaizen.composeapp.generated.resources.account_delete_final_confirmation_modal_button
import kaizen.composeapp.generated.resources.account_delete_final_confirmation_modal_subtitle
import kaizen.composeapp.generated.resources.account_delete_final_confirmation_modal_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun DeleteFinalConfirmationModalView(
    onAction: (AccountAction) -> Unit,
    isLoading: Boolean,
    error: String?
) {
    ConfirmationModalView(
        title = stringResource(Res.string.account_delete_final_confirmation_modal_title),
        subtitle = stringResource(Res.string.account_delete_final_confirmation_modal_subtitle),
        confirmationButtonText = stringResource(Res.string.account_delete_final_confirmation_modal_button),
        onConfirmed = {
            onAction(AccountAction.OnDeleteFinalConfirmationClicked)
        },
        onDismissed = {
            onAction(AccountAction.OnDeleteFinalConfirmationDismissed)
        },
        isConfirmationLoading = isLoading,
        type = ConfirmationModalType.DANGER,
        error = error
    )
}

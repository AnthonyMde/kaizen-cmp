package com.makapp.kaizen.ui.screens.login.component

import androidx.compose.runtime.Composable
import com.makapp.kaizen.ui.components.SuccessModalView
import com.makapp.kaizen.ui.screens.login.AuthAction
import kaizen.composeapp.generated.resources.Res
import kaizen.composeapp.generated.resources.auth_screen_forgot_password_reset_success_modal_button
import kaizen.composeapp.generated.resources.auth_screen_forgot_password_reset_success_modal_subtitle
import kaizen.composeapp.generated.resources.auth_screen_forgot_password_reset_success_modal_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun ResetPasswordSentModal(
    email: String,
    onAction: (AuthAction) -> Unit,
) {
    SuccessModalView(
        title = stringResource(Res.string.auth_screen_forgot_password_reset_success_modal_title),
        subtitle = stringResource(Res.string.auth_screen_forgot_password_reset_success_modal_subtitle, email),
        buttonText = stringResource(Res.string.auth_screen_forgot_password_reset_success_modal_button),
        onClick = {
            onAction(AuthAction.OnResetPasswordSentModalDismissed)
        }
    )
}

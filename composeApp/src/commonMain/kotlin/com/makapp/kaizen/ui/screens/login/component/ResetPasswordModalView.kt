package com.makapp.kaizen.ui.screens.login.component

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.makapp.kaizen.ui.components.FormErrorText
import com.makapp.kaizen.ui.components.LoadingButton
import com.makapp.kaizen.ui.screens.login.AuthAction
import kaizen.composeapp.generated.resources.Res
import kaizen.composeapp.generated.resources.auth_screen_forgot_password_reset_modal_button
import kaizen.composeapp.generated.resources.auth_screen_forgot_password_reset_modal_input_placeholder
import kaizen.composeapp.generated.resources.auth_screen_forgot_password_reset_modal_subtitle
import kaizen.composeapp.generated.resources.auth_screen_forgot_password_reset_modal_title
import kaizen.composeapp.generated.resources.confirmation_modal_view_cancel_button
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetPasswordModalView(
    onAction: (AuthAction) -> Unit,
    isLoading: Boolean,
    error: StringResource?,
    emailValue: String
) {
    BasicAlertDialog(
        onDismissRequest = {
            onAction(AuthAction.OnResetPasswordModalDismissed)
        },
        properties = DialogProperties(
            dismissOnClickOutside = true,
            dismissOnBackPress = true
        ),
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(24.dp),
        content = {
            val keyboard = LocalSoftwareKeyboardController.current
            val focus = LocalFocusManager.current

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .pointerInput(null) {
                        detectTapGestures(
                            onTap = {
                                focus.clearFocus()
                            }
                        )
                    }
            ) {
                Text(
                    stringResource(Res.string.auth_screen_forgot_password_reset_modal_title),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    stringResource(Res.string.auth_screen_forgot_password_reset_modal_subtitle),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = emailValue,
                    onValueChange = { text ->
                        onAction(AuthAction.OnResetPasswordEmailInputChanged(text))
                    },
                    singleLine = true,
                    placeholder = { Text(stringResource(Res.string.auth_screen_forgot_password_reset_modal_input_placeholder)) },
                    shape = RoundedCornerShape(16.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Email
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            resetPassword(onAction, keyboard)
                        }
                    )
                )

                Spacer(Modifier.height(16.dp))

                LoadingButton(
                    onClick = {
                        resetPassword(onAction, keyboard)
                    },
                    label = stringResource(Res.string.auth_screen_forgot_password_reset_modal_button),
                    isLoading = isLoading,
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth(),
                )
                error?.let {
                    FormErrorText(stringResource(error), textAlign = TextAlign.Center)
                }

                TextButton(
                    onClick = {
                        onAction(AuthAction.OnResetPasswordModalDismissed)
                    },
                    content = {
                        Text(
                            stringResource(Res.string.confirmation_modal_view_cancel_button),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        },
    )
}

private fun resetPassword(
    onAction: (AuthAction) -> Unit,
    keyboard: SoftwareKeyboardController?
) {
    keyboard?.hide()
    onAction(AuthAction.OnResetPasswordModalConfirmed)
}

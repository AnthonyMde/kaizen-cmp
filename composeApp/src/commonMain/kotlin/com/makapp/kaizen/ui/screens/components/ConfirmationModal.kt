package com.makapp.kaizen.ui.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import kaizen.composeapp.generated.resources.Res
import kaizen.composeapp.generated.resources.confirmation_modal_view_cancel_button
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmationModalView(
    title: String? = null,
    subtitle: String? = null,
    confirmationButtonText: String,
    onConfirmed: () -> Unit,
    onDismissed: () -> Unit = {},
    canBeDismissed: Boolean = true,
    isConfirmationLoading: Boolean = false,
    error: String? = null,
    type: ConfirmationModalType = ConfirmationModalType.WARNING
) {
    BasicAlertDialog(
        onDismissRequest = onDismissed,
        properties = DialogProperties(
            dismissOnClickOutside = canBeDismissed,
            dismissOnBackPress = canBeDismissed
        ),
        content = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(horizontal = 24.dp)
                    .padding(top = 24.dp)
            ) {
                title?.let {
                    Text(
                        title,
                        style = getTextStyle(type),
                        color = getTextColor(type),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }

                Spacer(Modifier.height(16.dp))

                subtitle?.let {
                    Text(
                        subtitle,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }

                Spacer(Modifier.height(16.dp))

                LoadingButton(
                    onClick = onConfirmed,
                    label = confirmationButtonText,
                    isLoading = isConfirmationLoading,
                    enabled = !isConfirmationLoading,
                    modifier = Modifier
                        .fillMaxWidth(),
                    buttonColors = getButtonColors(type)
                )
                error?.let {
                    FormErrorText(error, textAlign = TextAlign.Center)
                }

                Spacer(Modifier.height(4.dp))

                TextButton(
                    onClick = { onDismissed() },
                    content = {
                        Text(stringResource(Res.string.confirmation_modal_view_cancel_button),
                            style = MaterialTheme.typography.bodyMedium)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
    )
}

enum class ConfirmationModalType {
    WARNING, DANGER
}

@Composable
private fun getButtonColors(type: ConfirmationModalType): ButtonColors? {
    return when (type) {
        ConfirmationModalType.WARNING -> null
        ConfirmationModalType.DANGER -> {
            ButtonDefaults.buttonColors().copy(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError
            )
        }
    }
}

@Composable
private fun getTextColor(type: ConfirmationModalType): Color {
    return when (type) {
        ConfirmationModalType.WARNING -> MaterialTheme.colorScheme.primary
        ConfirmationModalType.DANGER -> MaterialTheme.colorScheme.error
    }
}

@Composable
private fun getTextStyle(type: ConfirmationModalType): TextStyle {
    return when (type) {
        ConfirmationModalType.WARNING -> MaterialTheme.typography.titleLarge
        ConfirmationModalType.DANGER -> MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
    }
}
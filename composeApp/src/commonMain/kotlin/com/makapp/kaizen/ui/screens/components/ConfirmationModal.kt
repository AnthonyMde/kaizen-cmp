package com.makapp.kaizen.ui.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmationModal(
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
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(24.dp)
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
                LoadingButton(
                    onClick = onConfirmed,
                    label = confirmationButtonText,
                    isLoading = isConfirmationLoading,
                    enabled = !isConfirmationLoading,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth(),
                    buttonColors = getButtonColors(type)
                )
                error?.let {
                    FormErrorText(error, textAlign = TextAlign.Center)
                }
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
package org.towny.kaizen.ui.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmationModal(
    title: String? = null,
    subtitle: String,
    confirmationButtonText: String,
    onConfirmed: () -> Unit,
    onDismissed: () -> Unit = {},
    canBeDismissed: Boolean = true,
    isConfirmationLoading: Boolean = false,
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
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                LoadingButton(
                    onClick = onConfirmed,
                    label = confirmationButtonText,
                    isLoading = isConfirmationLoading,
                    enabled = !isConfirmationLoading,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
    )
}

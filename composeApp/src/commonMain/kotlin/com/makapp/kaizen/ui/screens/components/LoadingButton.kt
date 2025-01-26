package com.makapp.kaizen.ui.screens.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun LoadingButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = true,
    label: String,
    buttonColors: ButtonColors? = null,
    shrinkToText: Boolean = false
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        content = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    label,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    modifier = getTextModifier(modifier, shrinkToText)
                )
                if (isLoading) CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp,
                    modifier = Modifier
                        .size(20.dp)
                )
            }
        },
        colors = buttonColors ?: ButtonDefaults.buttonColors(),
        modifier = modifier
    )
}

@Composable
private fun RowScope.getTextModifier(
    modifier: Modifier,
    shrinkToText: Boolean
): Modifier {
    return if (shrinkToText) {
        modifier
    } else {
        modifier.weight(1f)
    }
}

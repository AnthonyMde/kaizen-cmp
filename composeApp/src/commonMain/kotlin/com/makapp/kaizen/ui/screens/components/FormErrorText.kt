package com.makapp.kaizen.ui.screens.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun FormErrorText(
    message: String,
    textAlign: TextAlign? = null,
    modifier: Modifier = Modifier
) {
    Text(
        message,
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.bodyMedium,
        textAlign = textAlign,
        modifier = modifier
    )
}
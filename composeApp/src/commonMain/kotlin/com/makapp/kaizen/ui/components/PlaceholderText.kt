package com.makapp.kaizen.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PlaceholderText(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
        modifier = modifier
    )
}

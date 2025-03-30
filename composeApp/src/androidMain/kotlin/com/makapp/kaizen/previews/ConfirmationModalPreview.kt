package com.makapp.kaizen.previews

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.makapp.kaizen.ui.screens.components.ConfirmationModalView

@Preview
@Composable
fun ConfirmationModalPreview() {
    ConfirmationModalView(
        "This is title",
        subtitle = "Are you sure that this line describe a real subtitle?",
        confirmationButtonText = "Confirm",
        onConfirmed = {},
        onDismissed = {}
    )
}

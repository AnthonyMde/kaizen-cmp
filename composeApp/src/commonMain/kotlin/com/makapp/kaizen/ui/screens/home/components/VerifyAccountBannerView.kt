package com.makapp.kaizen.ui.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kaizen.composeapp.generated.resources.Res
import kaizen.composeapp.generated.resources.home_header_view_email_verification_banner_text
import org.jetbrains.compose.resources.stringResource

@Composable
fun VerifyAccountBannerView() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.errorContainer)
            .padding(horizontal = 24.dp, vertical = 8.dp)
    ) {
        Text(
            text = stringResource(Res.string.home_header_view_email_verification_banner_text),
            style = MaterialTheme.typography.bodySmall)
    }
}

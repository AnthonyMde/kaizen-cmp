package com.makapp.kaizen.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BannerView(
    bannerType: BannerType,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .background(color = getBannerBackground(bannerType))
            .padding(horizontal = 24.dp, vertical = 8.dp)
    ) {
        content()
    }
}

enum class BannerType {
    WARNING, INFO
}

@Composable
private fun getBannerBackground(type: BannerType): Color {
    return when (type) {
        BannerType.WARNING -> MaterialTheme.colorScheme.errorContainer
        BannerType.INFO -> MaterialTheme.colorScheme.tertiaryContainer
    }
}
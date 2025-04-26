package com.makapp.kaizen.ui.screens.home.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.makapp.kaizen.ui.components.BannerType
import com.makapp.kaizen.ui.components.BannerView
import kaizen.composeapp.generated.resources.Res
import kaizen.composeapp.generated.resources.home_header_view_email_verification_banner_text
import org.jetbrains.compose.resources.stringResource

@Composable
fun VerifyAccountBannerView(
    modifier: Modifier = Modifier,
) {
    BannerView(
        bannerType = BannerType.WARNING,
        modifier = modifier,
    ) {
        Text(
            text = stringResource(Res.string.home_header_view_email_verification_banner_text),
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

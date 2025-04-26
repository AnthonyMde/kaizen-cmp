package com.makapp.kaizen.ui.screens.onboarding.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.makapp.kaizen.ui.components.LoadingButton
import kaizen.composeapp.generated.resources.Res
import kaizen.composeapp.generated.resources.onboarding_profile_screen_submit_button
import org.jetbrains.compose.resources.stringResource

@Composable
fun BoxScope.CreateAccountButton(
    onClick: () -> Unit,
    isFormSubmissionLoading: Boolean,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.Black.copy(alpha = 0.2f)
                    )
                )
            )
            .padding(top = 16.dp)
    ) {
        LoadingButton(
            onClick = { onClick() },
            enabled = !isFormSubmissionLoading,
            isLoading = isFormSubmissionLoading,
            label = stringResource(Res.string.onboarding_profile_screen_submit_button),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}
package org.towny.kaizen.previews

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.towny.kaizen.ui.screens.onboarding.OnboardingProfileScreen

@Preview
@Composable
fun OnboardingProfileScreenPreview() {
    OnboardingProfileScreen(
        selectedAvatarIndex = 0,
        onAvatarSelected = {}
    )
}
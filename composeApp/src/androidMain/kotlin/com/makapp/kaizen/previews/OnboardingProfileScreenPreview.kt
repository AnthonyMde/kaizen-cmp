package com.makapp.kaizen.previews

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.makapp.kaizen.ui.screens.onboarding.OnBoardingProfileScreenState
import com.makapp.kaizen.ui.screens.onboarding.OnboardingProfileScreen

@Preview
@Composable
fun OnboardingProfileScreenPreview() {
    OnboardingProfileScreen(
        onAction = {},
        state = OnBoardingProfileScreenState()
    )
}
package org.towny.kaizen.ui.screens.onboarding

sealed class OnBoardingProfileNavigationEvent {
    data object GoToHomeScreen : OnBoardingProfileNavigationEvent()
}
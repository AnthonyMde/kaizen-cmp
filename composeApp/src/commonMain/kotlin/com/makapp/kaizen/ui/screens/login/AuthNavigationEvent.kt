package com.makapp.kaizen.ui.screens.login

sealed class AuthNavigationEvent {
    data object GoToHomeScreen: AuthNavigationEvent()
    data object GoToOnboardingProfile: AuthNavigationEvent()
}
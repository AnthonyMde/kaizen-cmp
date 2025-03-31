package com.makapp.kaizen.ui.screens.login

sealed class AuthEvent {
    data object GoToHomeScreen: AuthEvent()
    data object GoToOnboardingProfile: AuthEvent()
}
package org.towny.kaizen.ui.screens.login

sealed class LoginNavigationEvent {
    data object GoToHomeScreen: LoginNavigationEvent()
    data object GoToOnboardingProfile: LoginNavigationEvent()
}
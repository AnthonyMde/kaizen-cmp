package com.makapp.kaizen.ui.screens.home

sealed class HomeNavigationEvent {
    data object PopToLogin : HomeNavigationEvent()
    data object GoToUserAccountCreation : HomeNavigationEvent()
}

package org.towny.kaizen.ui.screens.home

sealed class HomeNavigationEvent {
    data object PopToLogin : HomeNavigationEvent()
}

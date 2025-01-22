package com.makapp.kaizen.ui.screens.home

sealed class HomeAction {
    data class OnToggleChallenge(
        val userId: String,
        val challengeId: String,
        val isChecked: Boolean
    ) : HomeAction()

    data object OnAccountClicked : HomeAction()
    data object OnEmailVerified : HomeAction()
    data object OnCreateFirstChallengeClicked : HomeAction()
}

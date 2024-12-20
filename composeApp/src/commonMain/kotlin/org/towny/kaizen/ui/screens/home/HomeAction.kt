package org.towny.kaizen.ui.screens.home

sealed class HomeAction {
    data class OnToggleChallenge(
        val userId: String,
        val challengeId: String,
        val isChecked: Boolean
    ) : HomeAction()
}
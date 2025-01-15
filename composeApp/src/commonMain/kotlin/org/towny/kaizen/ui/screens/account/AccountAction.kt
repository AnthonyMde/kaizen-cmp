package org.towny.kaizen.ui.screens.account

sealed class AccountAction {
    data object OnNavigateUp : AccountAction()
    data object OnLogoutClicked : AccountAction()
    data object OnLogoutConfirmed : AccountAction()
    data object OnLogoutDismissed : AccountAction()
    data object GoToMyFriends : AccountAction()
    data object GoToCreateChallenge : AccountAction()
}
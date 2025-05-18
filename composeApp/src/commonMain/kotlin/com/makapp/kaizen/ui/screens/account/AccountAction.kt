package com.makapp.kaizen.ui.screens.account

sealed interface AccountAction {
    data object OnNavigateUp : AccountAction
    data object OnLogoutClicked : AccountAction
    data object OnLogoutConfirmed : AccountAction
    data object OnLogoutDismissed : AccountAction
    data object GoToMyFriends : AccountAction
    data object GoToCreateChallenge : AccountAction
    data object OnDeleteAccountClicked : AccountAction
    data object OnDeleteAccountConfirmed : AccountAction
    data object OnDeleteAccountDismissed : AccountAction
    data object OnDeleteFinalConfirmationClicked: AccountAction
    data object OnDeleteFinalConfirmationDismissed: AccountAction
    data object OnProfileRowClicked: AccountAction
}
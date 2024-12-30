package org.towny.kaizen.ui.screens.account

sealed class AccountAction {
    data object OnNavigateUp : AccountAction()
    data object OnLogout : AccountAction()
}
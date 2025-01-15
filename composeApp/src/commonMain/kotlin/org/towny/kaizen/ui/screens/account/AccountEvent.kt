package org.towny.kaizen.ui.screens.account

sealed class AccountEvent {
    data object PopToLogin : AccountEvent()
}
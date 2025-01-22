package com.makapp.kaizen.ui.screens.account

sealed class AccountEvent {
    data object PopToLogin : AccountEvent()
}
package org.towny.kaizen.ui.screens.login

sealed class LoginAction {
    data class OnLoginInputTextChanged(val text: String): LoginAction()
    data class OnLoginSubmit(val username: String): LoginAction()
}
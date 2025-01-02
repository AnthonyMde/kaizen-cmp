package org.towny.kaizen.ui.screens.login

sealed class LoginAction {
    data class OnEmailInputTextChanged(val text: String): LoginAction()
    data class OnPasswordInputTextChanged(val password: String): LoginAction()
    data class OnLoginSubmit(val email: String, val password: String): LoginAction()
}
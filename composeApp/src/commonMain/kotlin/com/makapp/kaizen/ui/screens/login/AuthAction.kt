package com.makapp.kaizen.ui.screens.login

sealed class AuthAction {
    data class OnEmailInputTextChanged(val text: String): AuthAction()
    data class OnPasswordInputTextChanged(val password: String): AuthAction()
    data class OnAuthSubmit(val email: String, val password: String): AuthAction()
}
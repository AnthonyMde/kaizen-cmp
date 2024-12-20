package org.towny.kaizen.ui.screens.login

data class LoginScreenState(
    val loginInput: String = "",
    val onSubmitLoading: Boolean = false,
    val errorMessage: String? = null,
    val goToHomeScreen: Boolean = false
)
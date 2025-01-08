package org.towny.kaizen.ui.screens.login

data class AuthScreenState(
    val emailInputValue: String = "",
    val emailInputError: String? = null,
    val passwordInputValue: String = "",
    val passwordInputError: String? = null,
    val onSubmitLoading: Boolean = false,
)
package com.makapp.kaizen.ui.screens.login

import org.jetbrains.compose.resources.StringResource

data class AuthScreenState(
    val emailInputValue: String = "",
    val emailInputError: StringResource? = null,
    val passwordInputValue: String = "",
    val passwordInputError: StringResource? = null,
    val onSubmitLoading: Boolean = false,
    val isResetPasswordModalDisplayed: Boolean = false,
    val isResetPasswordRequestLoading: Boolean = false,
    val resetPasswordError: StringResource? = null,
    val isResetPasswordSentModalDisplayed: Boolean = false,
    val emailToSendForgotPasswordResetLink: String = ""
)
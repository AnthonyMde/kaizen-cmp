package com.makapp.kaizen.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.makapp.kaizen.domain.exceptions.DomainException
import com.makapp.kaizen.domain.models.Resource
import com.makapp.kaizen.domain.usecases.auth.AuthenticateUserUseCase
import com.makapp.kaizen.domain.usecases.auth.SendResetPasswordEmailUseCase
import kaizen.composeapp.generated.resources.Res
import kaizen.composeapp.generated.resources.auth_screen_empty_email_error
import kaizen.composeapp.generated.resources.auth_screen_empty_password_error
import kaizen.composeapp.generated.resources.auth_screen_not_authorized_error
import kaizen.composeapp.generated.resources.auth_screen_reset_password_error
import kaizen.composeapp.generated.resources.auth_screen_send_email_error
import kaizen.composeapp.generated.resources.auth_screen_weak_password_error
import kaizen.composeapp.generated.resources.auth_screen_wrong_credentials_error
import kaizen.composeapp.generated.resources.unknown_error
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource

class AuthViewModel(
    private val authenticateUserUseCase: AuthenticateUserUseCase,
    private val sendResetPasswordEmailUseCase: SendResetPasswordEmailUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(AuthScreenState())
    val state = _state.asStateFlow()

    private val _events = MutableSharedFlow<AuthEvent>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_LATEST
    )
    val events = _events.asSharedFlow()

    fun onAction(action: AuthAction) {
        when (action) {
            is AuthAction.OnEmailInputTextChanged -> {
                _state.update {
                    it.copy(
                        emailInputValue = action.text,
                        emailInputError = null,
                        emailToSendForgotPasswordResetLink = action.text
                    )
                }
            }

            is AuthAction.OnPasswordInputTextChanged -> {
                _state.update {
                    it.copy(
                        passwordInputValue = action.password,
                        passwordInputError = null
                    )
                }
            }

            is AuthAction.OnAuthSubmit -> {
                val email = action.email.trim()
                val password = action.password.trim()

                if (!requiredFields(email, password)) {
                    return
                }

                authenticateUser(email, password)
            }

            AuthAction.OnForgetPasswordClicked -> {
                _state.update {
                    it.copy(isResetPasswordModalDisplayed = true)
                }
            }

            AuthAction.OnResetPasswordModalConfirmed -> {
                sendResetPasswordEmail()
            }

            AuthAction.OnResetPasswordModalDismissed -> {
                _state.update {
                    it.copy(isResetPasswordModalDisplayed = false)
                }
            }

            AuthAction.OnResetPasswordSentModalDismissed -> {
                _state.update {
                    it.copy(isResetPasswordSentModalDisplayed = false)
                }
            }

            is AuthAction.OnResetPasswordEmailInputChanged -> {
               _state.update {
                   it.copy(emailToSendForgotPasswordResetLink = action.text)
               }
            }
        }
    }

    private fun sendResetPasswordEmail() = viewModelScope.launch {
        val email = _state.value.emailToSendForgotPasswordResetLink

        sendResetPasswordEmailUseCase(email).collectLatest { result ->
            when (result) {
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isResetPasswordRequestLoading = false,
                            resetPasswordError = Res.string.auth_screen_reset_password_error
                        )
                    }
                }

                is Resource.Loading -> {
                    _state.update {
                        it.copy(
                            isResetPasswordRequestLoading = true,
                            resetPasswordError = null
                        )
                    }
                }
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            isResetPasswordRequestLoading = false,
                            isResetPasswordModalDisplayed = false,
                            isResetPasswordSentModalDisplayed = true
                        )
                    }
                }
            }
        }
    }

    private fun requiredFields(email: String, password: String): Boolean {
        val isEmailEmpty = email.isBlank()
        val isPasswordEmpty = password.isBlank()
        if (isEmailEmpty) {
            _state.update {
                it.copy(emailInputError = Res.string.auth_screen_empty_email_error)
            }
        }
        if (isPasswordEmpty) {
            _state.update {
                it.copy(passwordInputError = Res.string.auth_screen_empty_password_error)
            }
        }

        return !isEmailEmpty && !isPasswordEmpty
    }

    private fun authenticateUser(email: String, password: String) = viewModelScope.launch {
        authenticateUserUseCase(email, password)
            .collectLatest { result ->
                when (result) {
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                onSubmitLoading = false,
                                passwordInputError = getLoginErrorMessage(result.throwable)
                            )
                        }
                    }

                    is Resource.Loading -> {
                        _state.update {
                            it.copy(
                                onSubmitLoading = true,
                                emailInputError = null,
                                passwordInputError = null
                            )
                        }
                    }

                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                onSubmitLoading = false,
                            )
                        }

                        if (result.data?.isSignUp == true)
                            _events.tryEmit(AuthEvent.GoToOnboardingProfile)
                        else
                            _events.tryEmit(AuthEvent.GoToHomeScreen)
                    }
                }
            }
    }

    private fun getLoginErrorMessage(throwable: Throwable?): StringResource {
        return when (throwable) {
            is DomainException.Auth.PasswordIsEmpty -> Res.string.auth_screen_empty_password_error
            is DomainException.Auth.UserNotAuthorized -> Res.string.auth_screen_not_authorized_error
            is DomainException.Auth.InvalidCredentials -> Res.string.auth_screen_wrong_credentials_error
            is DomainException.Auth.WeakPassword -> Res.string.auth_screen_weak_password_error
            is DomainException.Auth.FailedToSendEmailVerification -> Res.string.auth_screen_send_email_error
            else -> Res.string.unknown_error
        }
    }
}
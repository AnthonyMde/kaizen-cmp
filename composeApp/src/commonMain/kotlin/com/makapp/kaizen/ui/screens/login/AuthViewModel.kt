package com.makapp.kaizen.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.makapp.kaizen.domain.exceptions.DomainException
import com.makapp.kaizen.domain.models.Resource
import com.makapp.kaizen.domain.services.AuthenticateService
import kaizen.composeapp.generated.resources.Res
import kaizen.composeapp.generated.resources.auth_screen_empty_email_error
import kaizen.composeapp.generated.resources.auth_screen_empty_password_error
import kaizen.composeapp.generated.resources.auth_screen_not_authorized_error
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
    private val authenticateService: AuthenticateService,
) : ViewModel() {
    private val _authScreenState = MutableStateFlow(AuthScreenState())
    val authScreenState = _authScreenState.asStateFlow()

    private val _navigationEvents = MutableSharedFlow<AuthNavigationEvent>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_LATEST
    )
    val navigationEvents = _navigationEvents.asSharedFlow()

    fun onAction(action: AuthAction) {
        when (action) {
            is AuthAction.OnEmailInputTextChanged -> {
                _authScreenState.update {
                    it.copy(
                        emailInputValue = action.text,
                        emailInputError = null
                    )
                }
            }

            is AuthAction.OnPasswordInputTextChanged -> {
                _authScreenState.update {
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

                viewModelScope.launch {
                    authenticateService(email, password)
                        .collectLatest { result ->
                            when (result) {
                                is Resource.Error -> {
                                    _authScreenState.update {
                                        it.copy(
                                            onSubmitLoading = false,
                                            passwordInputError = getLoginErrorMessage(result.throwable)
                                        )
                                    }
                                }

                                is Resource.Loading -> {
                                    _authScreenState.update {
                                        it.copy(
                                            onSubmitLoading = true,
                                            emailInputError = null,
                                            passwordInputError = null
                                        )
                                    }
                                }

                                is Resource.Success -> {
                                    _authScreenState.update {
                                        it.copy(
                                            onSubmitLoading = false,
                                        )
                                    }

                                    if (result.data?.isSignUp == true)
                                        _navigationEvents.tryEmit(AuthNavigationEvent.GoToOnboardingProfile)
                                    else
                                        _navigationEvents.tryEmit(AuthNavigationEvent.GoToHomeScreen)
                                }
                            }
                        }
                }
            }
        }
    }

    private fun requiredFields(email: String, password: String): Boolean {
        val isEmailEmpty = email.isBlank()
        val isPasswordEmpty = password.isBlank()
        if (isEmailEmpty) {
            _authScreenState.update {
                it.copy(emailInputError = Res.string.auth_screen_empty_email_error)
            }
        }
        if (isPasswordEmpty) {
            _authScreenState.update {
                it.copy(passwordInputError = Res.string.auth_screen_empty_password_error)
            }
        }

        return !isEmailEmpty && !isPasswordEmpty
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
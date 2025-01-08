package org.towny.kaizen.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.towny.kaizen.domain.exceptions.DomainException
import org.towny.kaizen.domain.models.Resource
import org.towny.kaizen.domain.usecases.AuthenticateUseCase

class AuthViewModel(
    private val authenticateUseCase: AuthenticateUseCase,
) : ViewModel() {
    private val _authScreenState = MutableStateFlow(LoginScreenState())
    val authScreenState = _authScreenState.asStateFlow()

    private val _navigationEvents = MutableSharedFlow<LoginNavigationEvent>(
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
                    authenticateUseCase(email, password)
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
                                        _navigationEvents.tryEmit(LoginNavigationEvent.GoToOnboardingProfile)
                                    else
                                        _navigationEvents.tryEmit(LoginNavigationEvent.GoToHomeScreen)
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
                it.copy(emailInputError = "Email field should not be empty.")
            }
        }
        if (isPasswordEmpty) {
            _authScreenState.update {
                it.copy(passwordInputError = "Password field should not be empty.")
            }
        }

        return !isEmailEmpty && !isPasswordEmpty
    }

    private fun getLoginErrorMessage(throwable: Throwable?): String {
        return when (throwable) {
            is DomainException.Auth.PasswordIsEmpty -> "Enter something please."
            is DomainException.Auth.UserNotAuthorized -> "You are not authorized."
            is DomainException.Auth.InvalidCredentials -> "Email or password invalid."
            is DomainException.Auth.WeakPassword -> "You password should be at least 6 characters long."
            is DomainException.Auth.FailedToSendEmailVerification -> "Your account has been created but we failed to send your verification email."
            else -> throwable?.message ?: "Something went wrong."
        }
    }
}
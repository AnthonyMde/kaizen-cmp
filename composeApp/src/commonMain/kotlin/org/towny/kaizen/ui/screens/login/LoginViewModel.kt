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
import org.towny.kaizen.domain.services.LoginService

class LoginViewModel(
    private val loginService: LoginService,
) : ViewModel() {
    private val _loginScreenState = MutableStateFlow(LoginScreenState())
    val loginScreenState = _loginScreenState.asStateFlow()

    private val _navigationEvents = MutableSharedFlow<LoginNavigationEvent>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_LATEST
    )
    val navigationEvents = _navigationEvents.asSharedFlow()

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.OnLoginInputTextChanged -> {
                _loginScreenState.update { it.copy(loginInput = action.text) }
            }

            is LoginAction.OnLoginSubmit -> {
                viewModelScope.launch {
                    loginService.login(action.username).collectLatest { result ->
                        when (result) {
                            is Resource.Error -> {
                                _loginScreenState.update {
                                    it.copy(
                                        onSubmitLoading = false,
                                        errorMessage = getLoginErrorMessage(result.throwable)
                                    )
                                }
                            }

                            is Resource.Loading -> {
                                _loginScreenState.update {
                                    it.copy(
                                        onSubmitLoading = true,
                                        errorMessage = null
                                    )
                                }
                            }

                            is Resource.Success -> {
                                _loginScreenState.update {
                                    it.copy(
                                        onSubmitLoading = false,
                                        errorMessage = null,
                                        goToHomeScreen = true
                                    )
                                }
                                _navigationEvents.tryEmit(LoginNavigationEvent.GoToHomeScreen)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getLoginErrorMessage(throwable: Throwable?): String {
        return when (throwable) {
            is DomainException.Login.PasswordIsEmpty -> "Enter something please."
            is DomainException.Login.UserNotAuthorized -> "You are not authorized."
            else -> "Something went wrong"
        }
    }
}
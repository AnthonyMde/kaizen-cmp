package com.makapp.kaizen.ui.screens.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.makapp.kaizen.domain.exceptions.DomainException
import com.makapp.kaizen.domain.models.Resource
import com.makapp.kaizen.domain.usecases.CreateUserParams
import com.makapp.kaizen.domain.usecases.CreateUserUseCase

class OnboardingProfileViewModel(
    private val createUserUseCase: CreateUserUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(OnBoardingProfileScreenState())
    val state = _state.asStateFlow()

    private val _navigationEvents = MutableSharedFlow<OnBoardingProfileNavigationEvent>(
        onBufferOverflow = BufferOverflow.DROP_LATEST,
        replay = 0,
        extraBufferCapacity = 1
    )
    val navigationEvents = _navigationEvents.asSharedFlow()

    fun onAction(action: OnBoardingProfileAction) {
        when (action) {
            is OnBoardingProfileAction.OnAvatarSelected -> {
                _state.update {
                    it.copy(
                        avatarSelectedIndex = action.index
                    )
                }
            }

            is OnBoardingProfileAction.OnUsernameInputValueChanged -> {
                _state.update {
                    it.copy(
                        usernameInputValue = action.username,
                        usernameInputError = null
                    )
                }
            }

            is OnBoardingProfileAction.OnDisplayNameInputValueChanged -> {
                _state.update {
                    it.copy(
                        displayNameInputValue = action.displayName,
                        displayNameInputError = null
                    )
                }
            }

            OnBoardingProfileAction.OnSubmitProfile -> {
                val username = _state.value.usernameInputValue
                val displayName = _state.value.displayNameInputValue
                val chosenPictureIndex = _state.value.avatarSelectedIndex

                submitForm(username, displayName, chosenPictureIndex)
            }
        }
    }

    private fun submitForm(username: String, displayName: String, chosenPictureIndex: Int) =
        viewModelScope.launch {
            _state.update { it.copy(isFormSubmissionLoading = true) }

            val params = CreateUserParams(username, displayName, chosenPictureIndex)

            when (val result = createUserUseCase(params)) {
                is Resource.Error -> {
                    handleSubmitFormErrors(result)
                }

                is Resource.Success -> {
                    _navigationEvents.tryEmit(OnBoardingProfileNavigationEvent.GoToHomeScreen)
                }

                else -> {}
            }

            _state.update { it.copy(isFormSubmissionLoading = false) }
        }

    private fun handleSubmitFormErrors(result: Resource.Error<Unit>) {
        when (result.throwable) {
            is DomainException.User.Username -> {
                val errorMessage = when (result.throwable) {
                    DomainException.User.Username.CannotBeVerified -> "Sorry, we cannot verify your username by now, retry later."
                    DomainException.User.Username.AlreadyUsed -> "This username is already used."
                    DomainException.User.Username.IsEmpty -> "Username must not be empty."
                    DomainException.User.Username.IncorrectLength -> "Username must be 1-30 characters long."
                    DomainException.User.Username.DoubleSpecialCharNotAuthorized -> "Characters \"_\" and \".\" must not be doubled."
                    DomainException.User.Username.SpecialCharAtStartOrEndNotAuthorized -> "Characters \"_\" and \".\" must not start or end username."
                    DomainException.User.Username.SpecialCharNotAuthorized -> "Only special characters \"_\" and \".\" are authorized."
                }
                _state.update { it.copy(usernameInputError = errorMessage) }
            }

            is DomainException.User.DisplayName -> {
                val errorMessage = when (result.throwable) {
                    DomainException.User.DisplayName.IncorrectLength -> "Display name must be 1-30 characters long."
                }
                _state.update { it.copy(displayNameInputError = errorMessage) }
            }

            else -> {
                _state.update {
                    it.copy(
                        usernameInputError = result.throwable?.message ?: ""
                    )
                }
            }
        }
    }
}

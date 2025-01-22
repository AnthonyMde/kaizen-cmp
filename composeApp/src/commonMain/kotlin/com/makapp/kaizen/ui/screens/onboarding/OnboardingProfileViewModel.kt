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

            OnBoardingProfileAction.OnSubmitProfile -> {
                val username = _state.value.usernameInputValue.trim()
                val chosenPictureIndex = _state.value.avatarSelectedIndex

                submitForm(username, chosenPictureIndex)
            }
        }
    }

    private fun submitForm(username: String, chosenPictureIndex: Int) = viewModelScope.launch {
        _state.update { it.copy(isFormSubmissionLoading = true) }
        val params = CreateUserParams(username = username, pictureProfileIndex = chosenPictureIndex)

        when (val result = createUserUseCase(params)) {
            is Resource.Error -> {
                val errorMessage = when (result.throwable) {
                    is DomainException.User.Name.CannotBeVerified -> "Sorry, we cannot verify your username by now, retry later."
                    is DomainException.User.Name.AlreadyUsed-> "This username is already used."
                    is DomainException.User.Name.IsEmpty -> "Username must not be empty."
                    is DomainException.User.Name.IncorrectLength -> "Username must be 1-30 characters long."
                    is DomainException.User.Name.DoubleSpecialCharNotAuthorized -> "Characters \"_\" and \".\" must not be doubled."
                    is DomainException.User.Name.SpecialCharAtStartOrEndNotAuthorized -> "Characters \"_\" and \".\" must not start or end username."
                    is DomainException.User.Name.SpecialCharNotAuthorized -> "Only special characters \"_\" and \".\" are authorized."
                    else -> result.throwable?.message
                }
                _state.update { it.copy(usernameInputError = errorMessage) }
            }

            is Resource.Success -> {
                _navigationEvents.tryEmit(OnBoardingProfileNavigationEvent.GoToHomeScreen)
            }

            else -> {}
        }

        _state.update { it.copy(isFormSubmissionLoading = false) }
    }
}

package org.towny.kaizen.ui.screens.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.towny.kaizen.domain.exceptions.DomainException
import org.towny.kaizen.domain.models.Resource
import org.towny.kaizen.domain.usecases.CreateUserParams
import org.towny.kaizen.domain.usecases.CreateUserUseCase

class OnboardingProfileViewModel(
    private val createUserUseCase: CreateUserUseCase
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
                if (!requiredField(username)) {
                    return
                }
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
                    is DomainException.Auth.UsernameCannotBeVerified ->
                        "Sorry, we cannot verify your username by now, retry later."

                    else -> result.throwable?.message
                }
                _state.update { it.copy(formSubmissionError = errorMessage) }
            }

            is Resource.Success -> {
                _navigationEvents.tryEmit(OnBoardingProfileNavigationEvent.GoToHomeScreen)
            }

            else -> {}
        }

        _state.update { it.copy(isFormSubmissionLoading = false) }
    }

    private fun requiredField(username: String): Boolean {
        if (username.isBlank()) {
            _state.update {
                it.copy(
                    usernameInputError = "Username should not be empty."
                )
            }
        }

        return username.isNotBlank()
    }
}

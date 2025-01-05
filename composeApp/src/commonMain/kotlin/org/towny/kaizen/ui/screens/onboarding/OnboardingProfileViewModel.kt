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

class OnboardingProfileViewModel: ViewModel() {
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
                _state.update { it.copy(
                    avatarSelectedIndex = action.index
                ) }
            }

            is OnBoardingProfileAction.OnUsernameInputValueChanged -> {
                _state.update { it.copy(
                    usernameInputValue = action.username,
                    usernameInputError = null
                ) }
            }

            OnBoardingProfileAction.OnSubmitProfile -> {
                if (!requiredField(_state.value.usernameInputValue.trim())) {
                    return
                }
                _state.update { it.copy(
                    isFormSubmissionLoading = true
                ) }
                viewModelScope.launch {
                    // TODO: check if username is available
                    // TODO: update auth user + create firestore user
                    _state.update { it.copy(
                        isFormSubmissionLoading = false
                    ) }
                    _navigationEvents.tryEmit(OnBoardingProfileNavigationEvent.GoToHomeScreen)
                }
            }
        }
    }

    private fun requiredField(username: String): Boolean {
        if (username.isBlank()) {
            _state.update { it.copy(
                usernameInputError = "Username should not be empty."
            ) }
        }

        return username.isNotBlank()
    }
}

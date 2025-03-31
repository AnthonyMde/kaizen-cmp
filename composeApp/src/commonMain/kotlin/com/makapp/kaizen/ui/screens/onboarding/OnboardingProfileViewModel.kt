package com.makapp.kaizen.ui.screens.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.makapp.kaizen.domain.exceptions.DomainException
import com.makapp.kaizen.domain.models.Resource
import com.makapp.kaizen.domain.usecases.user.CreateUserParams
import com.makapp.kaizen.domain.usecases.user.CreateUserUseCase
import kaizen.composeapp.generated.resources.Res
import kaizen.composeapp.generated.resources.onboarding_profile_screen_displayname_length_error
import kaizen.composeapp.generated.resources.onboarding_profile_screen_username_already_used_error
import kaizen.composeapp.generated.resources.onboarding_profile_screen_username_cannot_be_verified_error
import kaizen.composeapp.generated.resources.onboarding_profile_screen_username_double_special_error
import kaizen.composeapp.generated.resources.onboarding_profile_screen_username_empty_error
import kaizen.composeapp.generated.resources.onboarding_profile_screen_username_length_error
import kaizen.composeapp.generated.resources.onboarding_profile_screen_username_special_error
import kaizen.composeapp.generated.resources.onboarding_profile_screen_username_start_special_error
import kaizen.composeapp.generated.resources.unknown_error
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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
                    DomainException.User.Username.CannotBeVerified -> Res.string.onboarding_profile_screen_username_cannot_be_verified_error
                    DomainException.User.Username.AlreadyUsed -> Res.string.onboarding_profile_screen_username_already_used_error
                    DomainException.User.Username.IsEmpty -> Res.string.onboarding_profile_screen_username_empty_error
                    DomainException.User.Username.IncorrectLength -> Res.string.onboarding_profile_screen_username_length_error
                    DomainException.User.Username.DoubleSpecialCharNotAuthorized -> Res.string.onboarding_profile_screen_username_double_special_error
                    DomainException.User.Username.SpecialCharAtStartOrEndNotAuthorized -> Res.string.onboarding_profile_screen_username_start_special_error
                    DomainException.User.Username.SpecialCharNotAuthorized -> Res.string.onboarding_profile_screen_username_special_error
                }
                _state.update { it.copy(usernameInputError = errorMessage) }
            }

            is DomainException.User.DisplayName -> {
                val errorMessage = when (result.throwable) {
                    DomainException.User.DisplayName.IncorrectLength -> Res.string.onboarding_profile_screen_displayname_length_error
                }
                _state.update { it.copy(displayNameInputError = errorMessage) }
            }

            else -> {
                _state.update {
                    it.copy(
                        usernameInputError = Res.string.unknown_error
                    )
                }
            }
        }
    }
}

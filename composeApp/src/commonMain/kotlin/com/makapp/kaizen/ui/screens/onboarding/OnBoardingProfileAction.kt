package com.makapp.kaizen.ui.screens.onboarding

sealed class OnBoardingProfileAction {
    data class OnAvatarSelected(val index: Int) : OnBoardingProfileAction()
    data class OnUsernameInputValueChanged(val username: String) : OnBoardingProfileAction()
    data class OnDisplayNameInputValueChanged(val displayName: String) : OnBoardingProfileAction()
    data object OnSubmitProfile : OnBoardingProfileAction()
}
package org.towny.kaizen.ui.screens.onboarding

data class OnBoardingProfileScreenState(
    val avatarSelectedIndex: Int = 0,
    val usernameInputValue: String = "",
    val usernameInputError: String? = null,
    val isFormSubmissionLoading: Boolean = false
)
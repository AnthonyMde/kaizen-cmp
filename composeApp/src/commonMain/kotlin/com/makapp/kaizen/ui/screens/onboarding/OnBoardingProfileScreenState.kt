package com.makapp.kaizen.ui.screens.onboarding

import com.makapp.kaizen.ui.resources.avatars
import kotlin.random.Random

data class OnBoardingProfileScreenState(
    val avatarSelectedIndex: Int = Random.nextInt(until = avatars.lastIndex),
    val usernameInputValue: String = "",
    val usernameInputError: String? = null,
    val displayNameInputValue: String = "",
    val displayNameInputError: String? = null,
    val isFormSubmissionLoading: Boolean = false,
)
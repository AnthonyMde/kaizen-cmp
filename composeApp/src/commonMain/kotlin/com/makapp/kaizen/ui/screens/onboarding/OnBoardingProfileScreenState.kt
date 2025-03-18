package com.makapp.kaizen.ui.screens.onboarding

import com.makapp.kaizen.ui.resources.avatars
import org.jetbrains.compose.resources.StringResource
import kotlin.random.Random

data class OnBoardingProfileScreenState(
    val avatarSelectedIndex: Int = Random.nextInt(until = avatars.lastIndex),
    val usernameInputValue: String = "",
    val usernameInputError: StringResource? = null,
    val displayNameInputValue: String = "",
    val displayNameInputError: StringResource? = null,
    val isFormSubmissionLoading: Boolean = false,
)
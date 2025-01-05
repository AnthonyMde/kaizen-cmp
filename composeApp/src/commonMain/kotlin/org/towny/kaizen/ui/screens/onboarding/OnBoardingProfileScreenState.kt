package org.towny.kaizen.ui.screens.onboarding

import org.towny.kaizen.ui.resources.avatars
import kotlin.random.Random

data class OnBoardingProfileScreenState(
    val avatarSelectedIndex: Int = Random.nextInt(until = avatars.lastIndex),
    val usernameInputValue: String = "",
    val usernameInputError: String? = null,
    val isFormSubmissionLoading: Boolean = false
)
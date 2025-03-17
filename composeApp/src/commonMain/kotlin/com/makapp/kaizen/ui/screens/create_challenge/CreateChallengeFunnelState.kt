package com.makapp.kaizen.ui.screens.create_challenge

import org.jetbrains.compose.resources.StringResource

data class CreateChallengeFunnelState(
    // Infos Screen
    val challengeNameInputValue: String = "",
    val challengeNameInputError: StringResource? = null,
    val numberOfLivesValue: Int = 0,
    val minimumLives: Int = 0,

    val isUpdateInfosLoading: Boolean = false,

    // Expectations Screen
    val expectationsInputValue: String = "",
    val isExpectationUpdateLoading: Boolean = false,
    val expectationUpdateError: String? = null,

    // Commitment Screen
    val commitmentInputValue: String = "",
    val isFormSubmissionLoading: Boolean = false,
    val formSubmissionError: StringResource? = null,
)
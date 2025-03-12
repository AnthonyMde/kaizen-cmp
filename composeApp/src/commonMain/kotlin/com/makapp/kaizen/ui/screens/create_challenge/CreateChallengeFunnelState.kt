package com.makapp.kaizen.ui.screens.create_challenge

data class CreateChallengeFunnelState(
    val challengeNameInputValue: String = "",
    val numberOfErrorsInputValue: String = "",
    val commitmentInputValue: String = "",
    val isFormSubmissionLoading: Boolean = false,

    val challengeNameInputError: String? = null,
    val numberOfErrorsInputError: String? = null,
    val formSubmissionError: String? = null
)
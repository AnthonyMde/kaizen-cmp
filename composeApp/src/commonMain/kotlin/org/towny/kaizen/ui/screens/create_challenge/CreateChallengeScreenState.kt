package org.towny.kaizen.ui.screens.create_challenge

data class CreateChallengeScreenState(
    val challengeNameInputValue: String = "",
    val numberOfErrorsInputValue: String = "",
    val isFormSubmissionLoading: Boolean = false,

    val challengeNameInputError: String? = null,
    val numberOfErrorsInputError: String? = null,
    val formSubmissionError: String? = null
)
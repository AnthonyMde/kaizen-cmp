package com.makapp.kaizen.ui.screens.create_challenge

data class CreateChallengeFunnelState(
    // Infos Screen
    val challengeNameInputValue: String = "",
    val numberOfErrorsInputValue: String = "",
    val challengeNameInputError: String? = null,
    val numberOfErrorsInputError: String? = null,

    val isUpdateInfosLoading: Boolean = false,

    // Expectations Screen
    val expectationsInputValue: String = "",
    val isExpectationUpdateLoading: Boolean = false,
    val expectationUpdateError: String? = null,

    // Commitment Screen
    val commitmentInputValue: String = "",
    val isFormSubmissionLoading: Boolean = false,
    val formSubmissionError: String? = null,
)
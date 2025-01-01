package org.towny.kaizen.ui.screens.create_challenge

data class CreateChallengeScreenState(
    val challengeTitleInputValue: String = "",
    val numberOfErrorsInputValue: String = "",
    val challengeTitleInputError: String? = null,
    val numberOfErrorsInputError: String? = null
)
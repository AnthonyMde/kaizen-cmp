package com.makapp.kaizen.ui.screens.create_challenge

sealed class CreateChallengeAction {
    data class OnNameInputValueChanged(val title: String) : CreateChallengeAction()
    data class OnNumberOfErrorsInputValueChanged(val numberOfErrors: String) : CreateChallengeAction()

    data object OnCreateChallengeFormSubmit : CreateChallengeAction()

    data object OnNavigateUp : CreateChallengeAction()
}
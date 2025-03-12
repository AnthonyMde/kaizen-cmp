package com.makapp.kaizen.ui.screens.create_challenge

sealed class CreateChallengeAction {
    data class OnNameInputValueChanged(val title: String) : CreateChallengeAction()
    data class OnNumberOfErrorsInputValueChanged(val numberOfErrors: String) : CreateChallengeAction()
    data class OnCommitmentInputValueChanged(val commitment: String) : CreateChallengeAction()

    data object OnCreateChallengeFormSubmit : CreateChallengeAction()

    data object OnNavigateUp : CreateChallengeAction()
    data object GoToCommitmentStep : CreateChallengeAction()
}
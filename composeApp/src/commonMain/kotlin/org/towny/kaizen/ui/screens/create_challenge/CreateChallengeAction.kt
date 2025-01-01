package org.towny.kaizen.ui.screens.create_challenge

sealed class CreateChallengeAction {
    data class OnTitleInputValueChanged(val title: String) : CreateChallengeAction()
    data class OnNumberOfErrorsInputValueChanged(val numberOfErrors: String) : CreateChallengeAction()

    data object OnNavigateUp : CreateChallengeAction()
}
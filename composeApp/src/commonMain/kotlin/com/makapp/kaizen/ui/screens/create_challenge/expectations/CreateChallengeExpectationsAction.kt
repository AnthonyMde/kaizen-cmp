package com.makapp.kaizen.ui.screens.create_challenge.expectations

sealed class CreateChallengeExpectationsAction {
    data class OnExpectationsValueChange(val expectations: String) :
        CreateChallengeExpectationsAction()

    data class UpdateExpectations(
        val challengeId: String,
        val expectations: String
    ) : CreateChallengeExpectationsAction()

    data object GoToCommitmentStep : CreateChallengeExpectationsAction()
    data object NavigateUp : CreateChallengeExpectationsAction()
}

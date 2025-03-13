package com.makapp.kaizen.ui.screens.create_challenge.commitment

sealed class CreateChallengeCommitmentAction {
    data class OnCommitmentInputValueChanged(val commitment: String) :
        CreateChallengeCommitmentAction()

    data object OnFormSubmit : CreateChallengeCommitmentAction()
    data class OnUpdateCommitment(val challengeId: String) : CreateChallengeCommitmentAction()

    data object OnNavigateUp : CreateChallengeCommitmentAction()
}

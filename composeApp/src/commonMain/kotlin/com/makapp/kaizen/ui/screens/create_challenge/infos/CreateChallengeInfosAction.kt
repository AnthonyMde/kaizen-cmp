package com.makapp.kaizen.ui.screens.create_challenge.infos

sealed class CreateChallengeInfosAction {
    data class OnNameInputValueChanged(val title: String) : CreateChallengeInfosAction()
    data class OnNumberOfErrorsInputValueChanged(val numberOfErrors: String) : CreateChallengeInfosAction()

    data object OnNavigateUp : CreateChallengeInfosAction()
    data object GoToCommitmentStep : CreateChallengeInfosAction()
    data class OnUpdateInfos(val challengeId: String) : CreateChallengeInfosAction()
}
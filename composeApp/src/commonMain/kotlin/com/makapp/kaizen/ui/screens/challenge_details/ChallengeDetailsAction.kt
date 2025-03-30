package com.makapp.kaizen.ui.screens.challenge_details

import com.makapp.kaizen.domain.models.challenge.Challenge

sealed class ChallengeDetailsAction {
    data class GoToChallengeInfos(val title: String, val maxLives: Int) : ChallengeDetailsAction()
    data object OnNavigateUp : ChallengeDetailsAction()
    data class GoToChallengeExpectations(val expectations: String) : ChallengeDetailsAction()
    data class GoToChallengeCommitment(val commitment: String) : ChallengeDetailsAction()

    data object OnStatusButtonClicked : ChallengeDetailsAction()
    data object OnBottomSheetDismissed : ChallengeDetailsAction()

    data object OnResumeChallengeClicked : ChallengeDetailsAction()
    data object OnPauseChallengeClicked : ChallengeDetailsAction()
    data object OnGiveUpChallengeClicked : ChallengeDetailsAction()

    data object OnChangeStatusModalDismissed : ChallengeDetailsAction()
    data class OnChangeStatusConfirmed(
        val challengeId: String,
        val newStatus: Challenge.Status
    ) : ChallengeDetailsAction()

    data object OnDeleteChallengeClicked : ChallengeDetailsAction()
    data object OnDeleteModalDismissed : ChallengeDetailsAction()
    data class OnDeleteChallengeConfirmed(val challengeId: String) : ChallengeDetailsAction()
}

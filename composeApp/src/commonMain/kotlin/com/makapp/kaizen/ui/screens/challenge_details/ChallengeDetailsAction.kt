package com.makapp.kaizen.ui.screens.challenge_details

import com.makapp.kaizen.domain.models.Challenge

sealed class ChallengeDetailsAction {
    data class GoToChallengeInfos(val title: String, val maxLives: Int) : ChallengeDetailsAction()
    data object OnNavigateUp : ChallengeDetailsAction()
    data class GoToChallengeExpectations(val expectations: String) : ChallengeDetailsAction()
    data class GoToChallengeCommitment(val commitment: String) : ChallengeDetailsAction()

    data object OnStatusButtonClicked : ChallengeDetailsAction()
    data object OnBottomSheetDismissed : ChallengeDetailsAction()

    data object OnChangeStatusClicked : ChallengeDetailsAction()
    data object OnChangeStatusModalDismissed : ChallengeDetailsAction()
    data class OnChangeStatusConfirmed(
        val challengeId: String,
        val currentStatus: Challenge.Status
    ) : ChallengeDetailsAction()

    data object OnResumeModalDismissed : ChallengeDetailsAction()

    data object OnGiveUpChallengeClicked : ChallengeDetailsAction()
    data object OnGiveUpModalDismissed : ChallengeDetailsAction()

    data object OnDeleteChallengeClicked : ChallengeDetailsAction()
    data object OnDeleteModalDismissed : ChallengeDetailsAction()
}

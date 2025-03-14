package com.makapp.kaizen.ui.screens.challenge_details

sealed class ChallengeDetailsAction {
    data object OnAbandonChallengeClicked : ChallengeDetailsAction()
    data object OnDeleteChallengeClicked : ChallengeDetailsAction()
    data object GoToChallengeInfos : ChallengeDetailsAction()
    data object OnNavigateUp : ChallengeDetailsAction()
    data class GoToChallengeExpectations(val expectations: String) : ChallengeDetailsAction()
    data class GoToChallengeCommitment(val commitment: String) : ChallengeDetailsAction()
}

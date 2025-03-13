package com.makapp.kaizen.ui.screens.challenge_details

sealed class ChallengeDetailsAction {
    data object OnNavigateUp : ChallengeDetailsAction()
    data class GoToChallengeExpectations(val expectations: String) : ChallengeDetailsAction()
    data object GoToChallengeCommitment : ChallengeDetailsAction()
}

package com.makapp.kaizen.ui.screens.challenge_details

sealed class ChallengeDetailsAction {
    data object OnDeleteChallengeClicked : ChallengeDetailsAction()
    data class GoToChallengeInfos(val title: String, val maxLives: Int) : ChallengeDetailsAction()
    data object OnNavigateUp : ChallengeDetailsAction()
    data class GoToChallengeExpectations(val expectations: String) : ChallengeDetailsAction()
    data class GoToChallengeCommitment(val commitment: String) : ChallengeDetailsAction()
    data object OnStatusButtonClicked : ChallengeDetailsAction()
    data object OnBottomSheetDismissed : ChallengeDetailsAction()
    data object OnPauseChallengeClicked : ChallengeDetailsAction()
    data object OnGiveUpChallengeClicked : ChallengeDetailsAction()
}

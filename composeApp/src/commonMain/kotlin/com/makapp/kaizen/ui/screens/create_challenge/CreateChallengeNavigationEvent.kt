package com.makapp.kaizen.ui.screens.create_challenge

sealed class CreateChallengeNavigationEvent {
    data object GoToCommitmentStep : CreateChallengeNavigationEvent()
    data object GoBackHome : CreateChallengeNavigationEvent()
}

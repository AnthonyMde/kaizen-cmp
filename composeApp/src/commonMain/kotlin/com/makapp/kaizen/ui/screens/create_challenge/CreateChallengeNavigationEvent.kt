package com.makapp.kaizen.ui.screens.create_challenge

sealed class CreateChallengeNavigationEvent {
    data object GoHome : CreateChallengeNavigationEvent()
    data object GoToCommitmentStep : CreateChallengeNavigationEvent()
}
package com.makapp.kaizen.ui.screens.create_challenge

sealed class CreateChallengeNavigationEvent {
    data object GoToExpectationsStep : CreateChallengeNavigationEvent()
    data object GoBackHome : CreateChallengeNavigationEvent()
    data object NavigateUp : CreateChallengeNavigationEvent()
}

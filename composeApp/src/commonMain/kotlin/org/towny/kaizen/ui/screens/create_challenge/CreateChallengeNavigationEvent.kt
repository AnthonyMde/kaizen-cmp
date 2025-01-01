package org.towny.kaizen.ui.screens.create_challenge

sealed class CreateChallengeNavigationEvent {
    data object GoHome : CreateChallengeNavigationEvent()
}
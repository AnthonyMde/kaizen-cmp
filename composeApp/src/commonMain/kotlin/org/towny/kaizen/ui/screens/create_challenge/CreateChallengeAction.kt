package org.towny.kaizen.ui.screens.create_challenge

sealed class CreateChallengeAction {
    data object OnNavigateUp : CreateChallengeAction()
}
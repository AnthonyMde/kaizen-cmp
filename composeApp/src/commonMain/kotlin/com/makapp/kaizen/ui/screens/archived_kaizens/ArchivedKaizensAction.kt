package com.makapp.kaizen.ui.screens.archived_kaizens

sealed interface ArchivedKaizensAction {
    data object OnNavigateUp : ArchivedKaizensAction
    data class GoToChallengeDetails(val id: String) : ArchivedKaizensAction
}
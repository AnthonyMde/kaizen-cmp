package com.makapp.kaizen.ui.screens.archived_kaizens

sealed interface ArchivedKaizensAction {
    data object OnNavigateUp : ArchivedKaizensAction
}
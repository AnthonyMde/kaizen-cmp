package com.makapp.kaizen.ui.screens.profile

sealed interface ProfileAction {
    data object OnNavigateUp : ProfileAction
}
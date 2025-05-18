package com.makapp.kaizen.ui.screens.archived_kaizens

import com.makapp.kaizen.domain.models.challenge.Challenge

sealed interface ArchivedKaizensUiState {
    data object Loading: ArchivedKaizensUiState
    data class Error(val message: String): ArchivedKaizensUiState
    data class Data(
        val failedKaizens: List<Challenge>,
        val abandonedKaizens: List<Challenge>,
    ): ArchivedKaizensUiState
    data object Empty: ArchivedKaizensUiState
}

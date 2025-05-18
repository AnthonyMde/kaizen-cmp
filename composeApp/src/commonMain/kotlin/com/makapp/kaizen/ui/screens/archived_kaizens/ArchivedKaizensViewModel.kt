package com.makapp.kaizen.ui.screens.archived_kaizens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.makapp.kaizen.domain.FlowHelper.stateIn
import com.makapp.kaizen.domain.models.Resource
import com.makapp.kaizen.domain.models.challenge.Challenge
import com.makapp.kaizen.domain.usecases.ObserveArchivedKaizensUseCase
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class ArchivedKaizensViewModel(
    observeArchivedKaizensUseCase: ObserveArchivedKaizensUseCase,
) : ViewModel() {
    val uiState = observeArchivedKaizensUseCase()
        .distinctUntilChanged()
        .map { challengesState ->
            mapToUiState(challengesState)
        }
        .stateIn(viewModelScope, ArchivedKaizensUiState.Loading)

    private fun mapToUiState(challengesState: Resource<List<Challenge>>): ArchivedKaizensUiState {
        return when (challengesState) {
            is Resource.Error -> {
                ArchivedKaizensUiState.Error(challengesState.throwable?.message ?: "") // TODO
            }
            is Resource.Loading -> {
                ArchivedKaizensUiState.Loading
            }
            is Resource.Success -> {
                val kaizens = challengesState.data ?: emptyList()
                val abandoned = kaizens.filter { it.isAbandoned() }
                val failed = kaizens.filter { it.isFailed() }

                if (kaizens.isNotEmpty()) {
                    ArchivedKaizensUiState.Data(
                        abandonedKaizens = abandoned,
                        failedKaizens = failed,
                    )
                } else {
                    ArchivedKaizensUiState.Empty
                }
            }
        }
    }
}
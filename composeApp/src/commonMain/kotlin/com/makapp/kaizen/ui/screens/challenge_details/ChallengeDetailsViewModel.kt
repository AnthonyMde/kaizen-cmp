package com.makapp.kaizen.ui.screens.challenge_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.makapp.kaizen.domain.models.Challenge
import com.makapp.kaizen.domain.models.Resource
import com.makapp.kaizen.domain.services.ChallengesService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChallengeDetailsViewModel(
    private val challengesService: ChallengesService
) : ViewModel() {
    private val _state = MutableStateFlow(ChallengeDetailsState())
    val state = _state.asStateFlow()

    fun onAction(action: ChallengeDetailsAction) {
        when (action) {

            else -> {}
        }
    }

    fun fetchChallengeDetails(id: String) = viewModelScope.launch {
        println("DEBUG: get challenge by id $id")
        challengesService.getChallengeById(id).collectLatest { result ->
            when (result) {
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isDetailsLoading = false,
                            challengeError = result.throwable?.message
                        )
                    }
                }

                is Resource.Loading -> {
                    _state.update { it.copy(isDetailsLoading = true) }
                }

                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            isDetailsLoading = false,
                            challenge = result.data
                        )
                    }
                }
            }
        }
    }

    fun getChallengeStatusText(status: Challenge.Status): String = when (status) {
        Challenge.Status.ON_GOING -> {
            "ONGOING"
        }

        Challenge.Status.PAUSED -> {
            "PAUSED"
        }
        Challenge.Status.DONE -> {
            "COMPLETED"
        }
        Challenge.Status.FAILED -> {
            "FAILED"
        }
    }
}

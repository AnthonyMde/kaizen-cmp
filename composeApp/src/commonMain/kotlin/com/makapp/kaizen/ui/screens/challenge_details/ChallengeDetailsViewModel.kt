package com.makapp.kaizen.ui.screens.challenge_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.makapp.kaizen.domain.models.Challenge
import com.makapp.kaizen.domain.models.Resource
import com.makapp.kaizen.domain.services.ChallengesService
import kaizen.composeapp.generated.resources.Res
import kaizen.composeapp.generated.resources.ic_broken_heart
import kaizen.composeapp.generated.resources.ic_heart
import kaizen.composeapp.generated.resources.ic_heart_plus
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource

class ChallengeDetailsViewModel(
    private val challengesService: ChallengesService
) : ViewModel() {
    private val _state = MutableStateFlow(ChallengeDetailsState())
    val state = _state.asStateFlow()

    fun onAction(action: ChallengeDetailsAction) {
        when (action) {
            ChallengeDetailsAction.OnStatusButtonClicked -> {
                _state.update { it.copy(
                    isBottomSheetOpened = true
                ) }
            }
            ChallengeDetailsAction.OnPauseChallengeClicked -> {
                _state.update { it.copy(
                    isBottomSheetOpened = false,
                    isPauseChallengeModalDisplayed = true
                ) }
            }
            ChallengeDetailsAction.OnPauseModalDismissed -> {
                _state.update { it.copy(
                    isPauseChallengeModalDisplayed = false
                ) }
            }
            ChallengeDetailsAction.OnPauseConfirmed -> {
                _state.update { it.copy(
                    isPauseRequestLoading = true
                ) }
                viewModelScope.launch {
                    delay(1000)
                    _state.update { it.copy(
                        isPauseRequestLoading = false,
                        isPauseChallengeModalDisplayed = false
                    ) }
                }
            }
            ChallengeDetailsAction.OnGiveUpChallengeClicked -> {
                _state.update { it.copy(
                    isBottomSheetOpened = false,
                    isGiveUpChallengeModalDisplayed = true
                ) }
            }
            ChallengeDetailsAction.OnBottomSheetDismissed -> {
                _state.update { it.copy(
                    isBottomSheetOpened = false
                ) }
            }
            ChallengeDetailsAction.OnDeleteChallengeClicked -> {
                _state.update { it.copy(
                    isGiveUpChallengeModalDisplayed = true
                ) }
            }
            else -> {}
        }
    }

    fun fetchChallengeDetails(id: String) = viewModelScope.launch {
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
            "Ongoing"
        }

        Challenge.Status.PAUSED -> {
            "Paused"
        }
        Challenge.Status.DONE -> {
            "Completed"
        }
        Challenge.Status.FAILED -> {
            "Failed"
        }
    }

    fun getHeartIcon(maxFailures: Int, failures: Int): DrawableResource {
        return when {
            failures > maxFailures -> Res.drawable.ic_broken_heart
            maxFailures < Challenge.MAX_POSSIBLE_FAILURES -> Res.drawable.ic_heart_plus
            else -> Res.drawable.ic_heart
        }
    }
}

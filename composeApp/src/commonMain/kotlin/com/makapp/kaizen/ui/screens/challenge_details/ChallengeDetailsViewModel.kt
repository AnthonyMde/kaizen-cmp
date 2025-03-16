package com.makapp.kaizen.ui.screens.challenge_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.makapp.kaizen.domain.models.Challenge
import com.makapp.kaizen.domain.models.Resource
import com.makapp.kaizen.domain.models.UpdateChallengeFields
import com.makapp.kaizen.domain.repository.ChallengesRepository
import com.makapp.kaizen.domain.services.ChallengesService
import kaizen.composeapp.generated.resources.Res
import kaizen.composeapp.generated.resources.ic_broken_heart
import kaizen.composeapp.generated.resources.ic_heart
import kaizen.composeapp.generated.resources.ic_heart_plus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource

class ChallengeDetailsViewModel(
    private val challengesService: ChallengesService,
    private val challengesRepository: ChallengesRepository
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
            ChallengeDetailsAction.OnResumeChallengeClicked -> {
                _state.update { it.copy(
                    isBottomSheetOpened = false,
                    isChangeStatusModalDisplayed = true,
                    newStatusRequested = Challenge.Status.ON_GOING
                ) }
            }
            ChallengeDetailsAction.OnPauseChallengeClicked -> {
                _state.update { it.copy(
                    isBottomSheetOpened = false,
                    isChangeStatusModalDisplayed = true,
                    newStatusRequested = Challenge.Status.PAUSED
                ) }
            }
            ChallengeDetailsAction.OnGiveUpChallengeClicked -> {
                _state.update { it.copy(
                    isBottomSheetOpened = false,
                    isChangeStatusModalDisplayed = true,
                    newStatusRequested = Challenge.Status.ABANDONED
                ) }
            }
            ChallengeDetailsAction.OnChangeStatusModalDismissed -> {
                _state.update { it.copy(
                    isChangeStatusModalDisplayed = false
                ) }
            }
            is ChallengeDetailsAction.OnChangeStatusConfirmed -> {
                changeStatus(action.challengeId, action.newStatus)
            }
            ChallengeDetailsAction.OnBottomSheetDismissed -> {
                _state.update { it.copy(
                    isBottomSheetOpened = false
                ) }
            }
            ChallengeDetailsAction.OnDeleteChallengeClicked -> {
                _state.update { it.copy(
                    isDeleteChallengeModalDisplayed = true
                ) }
            }
            else -> {}
        }
    }

    fun watchChallengeDetails(id: String) = viewModelScope.launch {
        _state.update { it.copy(isDetailsLoading = true) }
        challengesRepository.watchChallengeById(id).collectLatest { result ->
            _state.update { it.copy(isDetailsLoading = false) }
            when (result) {
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isDetailsLoading = false,
                            challengeError = result.throwable?.message
                        )
                    }
                }

                is Resource.Loading -> {}

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

    fun getHeartIcon(maxFailures: Int, failures: Int): DrawableResource {
        return when {
            failures > maxFailures -> Res.drawable.ic_broken_heart
            maxFailures < Challenge.MAX_POSSIBLE_FAILURES -> Res.drawable.ic_heart_plus
            else -> Res.drawable.ic_heart
        }
    }

    fun isHeartButtonClickable(isEditable: Boolean, challenge: Challenge): Boolean =
        isEditable && challenge.maxAuthorizedFailures < Challenge.MAX_POSSIBLE_FAILURES

    fun isChallengeEditable(challenge: Challenge?, readOnly: Boolean): Boolean =
        !readOnly && (challenge?.isPaused() == true || challenge?.isOngoing() == true)

    private fun changeStatus(
        challengeId: String,
        newStatusRequested: Challenge.Status
    ) {
        viewModelScope.launch {
            challengesRepository.update(
                id = challengeId,
                fields = UpdateChallengeFields(status = newStatusRequested)
            ).collectLatest { result ->
                when (result) {
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isChangeStatusRequestLoading = false,
                                changeStatusRequestError = result.throwable?.message
                            )
                        }
                    }

                    is Resource.Loading -> {
                        _state.update {
                            it.copy(
                                isChangeStatusRequestLoading = true
                            )
                        }
                    }

                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                isChangeStatusRequestLoading = false,
                                isChangeStatusModalDisplayed = false
                            )
                        }
                    }
                }
            }
        }
    }
}

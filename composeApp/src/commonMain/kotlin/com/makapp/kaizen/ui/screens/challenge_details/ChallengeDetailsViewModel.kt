package com.makapp.kaizen.ui.screens.challenge_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.makapp.kaizen.domain.models.Resource
import com.makapp.kaizen.domain.models.challenge.Challenge
import com.makapp.kaizen.domain.models.challenge.UpdateChallengeFields
import com.makapp.kaizen.domain.repository.ChallengesRepository
import com.makapp.kaizen.domain.repository.UsersRepository
import com.makapp.kaizen.domain.services.ChallengesService
import kaizen.composeapp.generated.resources.Res
import kaizen.composeapp.generated.resources.ic_broken_heart
import kaizen.composeapp.generated.resources.ic_heart
import kaizen.composeapp.generated.resources.ic_heart_plus
import kaizen.composeapp.generated.resources.unknown_error
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant
import org.jetbrains.compose.resources.DrawableResource

class ChallengeDetailsViewModel(
    private val challengesService: ChallengesService,
    private val challengesRepository: ChallengesRepository,
    private val usersRepository: UsersRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(ChallengeDetailsState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<ChallengeDetailsEvents>(
        extraBufferCapacity = 1,
        replay = 0,
        onBufferOverflow = BufferOverflow.DROP_LATEST
    )
    val event = _event.asSharedFlow()

    fun onAction(action: ChallengeDetailsAction) {
        when (action) {
            ChallengeDetailsAction.OnStatusButtonClicked -> {
                _state.update {
                    it.copy(
                        isBottomSheetOpened = true
                    )
                }
            }

            ChallengeDetailsAction.OnResumeChallengeClicked -> {
                _state.update {
                    it.copy(
                        isBottomSheetOpened = false,
                        isChangeStatusModalDisplayed = true,
                        newStatusRequested = Challenge.Status.ON_GOING
                    )
                }
            }

            ChallengeDetailsAction.OnPauseChallengeClicked -> {
                _state.update {
                    it.copy(
                        isBottomSheetOpened = false,
                        isChangeStatusModalDisplayed = true,
                        newStatusRequested = Challenge.Status.PAUSED
                    )
                }
            }

            ChallengeDetailsAction.OnGiveUpChallengeClicked -> {
                _state.update {
                    it.copy(
                        isBottomSheetOpened = false,
                        isChangeStatusModalDisplayed = true,
                        newStatusRequested = Challenge.Status.ABANDONED
                    )
                }
            }

            ChallengeDetailsAction.OnChangeStatusModalDismissed -> {
                _state.update {
                    it.copy(
                        isChangeStatusModalDisplayed = false
                    )
                }
            }

            is ChallengeDetailsAction.OnChangeStatusConfirmed -> {
                changeStatus(action.challengeId, action.newStatus)
            }

            ChallengeDetailsAction.OnBottomSheetDismissed -> {
                _state.update {
                    it.copy(
                        isBottomSheetOpened = false
                    )
                }
            }

            ChallengeDetailsAction.OnDeleteChallengeClicked -> {
                _state.update {
                    it.copy(
                        isDeleteChallengeModalDisplayed = true
                    )
                }
            }

            ChallengeDetailsAction.OnDeleteModalDismissed -> {
                _state.update {
                    it.copy(
                        isDeleteChallengeModalDisplayed = false
                    )
                }
            }

            is ChallengeDetailsAction.OnDeleteChallengeConfirmed -> {
                deleteChallenge(action.challengeId)
            }

            is ChallengeDetailsAction.OnForgotToCheckButtonClicked -> {
                forgotToCheckChallenge(action.challengeId)
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
                            challengeError = Res.string.unknown_error
                        )
                    }
                }

                is Resource.Loading -> {}

                is Resource.Success -> {
                    _state.update {
                        val challenge = result.data

                        it.copy(
                            isDetailsLoading = false,
                            challenge = challenge,
                            shouldDisplayForgotToCheckButton = getShouldDisplayForgotButton(challenge),
                        )
                    }
                }
            }
        }
    }

    // TODO: move logic only to backend
    private fun getShouldDisplayForgotButton(challenge: Challenge?): Boolean {
        challenge ?: return false

        return isFailureStillForgettable(challenge.lastFailureDate)
                && !challenge.didUseForgotFeatureToday
                && (challenge.isOngoing() || challenge.isFailed())
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

    // TODO: move this only to backend side (should wait ktor backend)
    private fun isFailureStillForgettable(date: LocalDate?): Boolean {
        date ?: return false // if null might be user never lost a life.

        val failureDateTime = date.atTime(4, 0)
            .toInstant(TimeZone.currentSystemDefault())

        val now = Clock.System.now()
        val hours = (now - failureDateTime).inWholeHours

        return hours < 24
    }

    private fun forgotToCheckChallenge(challengeId: String) = viewModelScope.launch {
        challengesRepository.forgotToCheckChallenge(challengeId)
            .collectLatest { result ->
                when (result) {
                    is Resource.Error -> {
                        // TODO
                        _state.update {
                            it.copy(
                                isForgotToCheckButtonLoading = false,
                            )
                        }
                    }
                    is Resource.Loading -> {
                        _state.update {
                            it.copy(
                                isForgotToCheckButtonLoading = true,
                            )
                        }
                    }
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                isForgotToCheckButtonLoading = false,
                            )
                        }
                    }
                }
            }
    }

    private fun deleteChallenge(challengeId: String) = viewModelScope.launch {
        challengesRepository.update(
            id = challengeId,
            fields = UpdateChallengeFields(
                isDeleted = true
            )
        ).collectLatest { result ->
            when (result) {
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isDeleteRequestLoading = false,
                            deleteRequestError = Res.string.unknown_error
                        )
                    }
                }

                is Resource.Loading -> {
                    _state.update {
                        it.copy(
                            isDeleteRequestLoading = true
                        )
                    }
                }

                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            isDeleteRequestLoading = false,
                            deleteRequestError = null,
                            isDeleteChallengeModalDisplayed = false
                        )
                    }
                    _event.tryEmit(ChallengeDetailsEvents.NavigateUp)
                }
            }
        }
    }

    private fun changeStatus(
        challengeId: String,
        newStatusRequested: Challenge.Status
    ) = viewModelScope.launch {
        challengesRepository.update(
            id = challengeId,
            fields = UpdateChallengeFields(status = newStatusRequested)
        ).collectLatest { result ->
            when (result) {
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isChangeStatusRequestLoading = false,
                            changeStatusRequestError = Res.string.unknown_error
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

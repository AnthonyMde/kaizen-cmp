package com.makapp.kaizen.ui.screens.create_challenge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.makapp.kaizen.domain.models.CreateChallengeForm
import com.makapp.kaizen.domain.models.Resource
import com.makapp.kaizen.domain.models.UpdateChallengeFields
import com.makapp.kaizen.domain.repository.ChallengesRepository
import com.makapp.kaizen.domain.services.ChallengesService
import com.makapp.kaizen.ui.screens.create_challenge.commitment.CreateChallengeCommitmentAction
import com.makapp.kaizen.ui.screens.create_challenge.expectations.CreateChallengeExpectationsAction
import com.makapp.kaizen.ui.screens.create_challenge.infos.CreateChallengeInfosAction
import kaizen.composeapp.generated.resources.Res
import kaizen.composeapp.generated.resources.challenge_info_screen_challenge_title_input_error
import kaizen.composeapp.generated.resources.unknown_error
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateChallengeViewModel(
    private val challengesService: ChallengesService,
    private val challengesRepository: ChallengesRepository
) : ViewModel() {
    companion object {
        const val MAX_CHALLENGE_TITLE_LENGTH = 20
        const val MAX_CHALLENGE_EXPECTATIONS_LENGTH = 250
        const val MAX_CHALLENGE_COMMITMENT_LENGTH = 250
        const val MAX_LIVES_ALLOWED = 12
        private const val MAX_CHALLENGE_ERRORS_LENGTH = 2
    }

    private val _createChallengeFunnelState = MutableStateFlow(CreateChallengeFunnelState())
    val createChallengeScreenState = _createChallengeFunnelState.asStateFlow()

    private val _navigationEvents = MutableSharedFlow<CreateChallengeNavigationEvent>(
        extraBufferCapacity = 1,
        replay = 0,
        onBufferOverflow = BufferOverflow.DROP_LATEST
    )
    val navigationEvents = _navigationEvents.asSharedFlow()

    fun onInfosAction(action: CreateChallengeInfosAction) {
        when (action) {
            is CreateChallengeInfosAction.OnNameInputValueChanged -> {
                val title = action.title.take(MAX_CHALLENGE_TITLE_LENGTH)
                _createChallengeFunnelState.update {
                    it.copy(
                        challengeNameInputValue = title,
                        challengeNameInputError = null
                    )
                }
            }

            is CreateChallengeInfosAction.OnNumberOfLivesChanged -> {
                val numberOfLives = action.numberOfLives.takeIf {
                    it in _createChallengeFunnelState.value.minimumLives..MAX_LIVES_ALLOWED
                }
                if (numberOfLives != null) {
                    _createChallengeFunnelState.update {
                        it.copy(
                            numberOfLivesValue = numberOfLives
                        )
                    }
                }
            }

            is CreateChallengeInfosAction.SetMinimumNumberOfLives -> {
                _createChallengeFunnelState.update {
                    it.copy(minimumLives = action.minimum)
                }
            }

            CreateChallengeInfosAction.GoToCommitmentStep -> {
                val name = _createChallengeFunnelState.value.challengeNameInputValue
                if (!requiredFields(name)) {
                    return
                }
                _navigationEvents.tryEmit(CreateChallengeNavigationEvent.GoToExpectationsStep)
            }

            is CreateChallengeInfosAction.OnUpdateInfos -> {
                val name = _createChallengeFunnelState.value.challengeNameInputValue
                val maxLives = _createChallengeFunnelState.value.numberOfLivesValue
                if (!requiredFields(name)) {
                    return
                }

                viewModelScope.launch {
                    challengesRepository.update(
                        action.challengeId, fields = UpdateChallengeFields(
                            name = name,
                            maxAuthorizedFailures = maxLives
                        )
                    ).collectLatest { result ->
                        when (result) {
                            is Resource.Error -> {
                                _createChallengeFunnelState.update {
                                    it.copy(
                                        isUpdateInfosLoading = false,
                                        challengeNameInputError = Res.string.unknown_error
                                    )
                                }
                            }

                            is Resource.Loading -> {
                                _createChallengeFunnelState.update {
                                    it.copy(
                                        isUpdateInfosLoading = true,
                                        challengeNameInputError = null
                                    )
                                }
                            }

                            is Resource.Success -> {
                                _createChallengeFunnelState.update {
                                    it.copy(
                                        isUpdateInfosLoading = false
                                    )
                                }
                                _navigationEvents.tryEmit(CreateChallengeNavigationEvent.NavigateUp)
                            }
                        }
                    }
                }
            }

            else -> {}
        }
    }

    fun onExpectationsAction(action: CreateChallengeExpectationsAction) {
        when (action) {
            is CreateChallengeExpectationsAction.OnExpectationsValueChange -> {
                val expectations = action.expectations.take(MAX_CHALLENGE_EXPECTATIONS_LENGTH)
                _createChallengeFunnelState.update {
                    it.copy(expectationsInputValue = expectations)
                }
            }

            is CreateChallengeExpectationsAction.OnUpdateExpectations -> {
                val expectations = _createChallengeFunnelState.value.expectationsInputValue
                viewModelScope.launch {
                    challengesRepository.update(
                        id = action.challengeId,
                        fields = UpdateChallengeFields(expectations = expectations)
                    ).collectLatest { result ->
                        when (result) {
                            is Resource.Error -> {
                                _createChallengeFunnelState.update {
                                    it.copy(
                                        isExpectationUpdateLoading = false,
                                        expectationUpdateError = result.throwable?.message
                                    )
                                }
                            }

                            is Resource.Loading -> {
                                _createChallengeFunnelState.update {
                                    it.copy(
                                        isExpectationUpdateLoading = true,
                                        expectationUpdateError = null
                                    )
                                }
                            }

                            is Resource.Success -> {
                                _createChallengeFunnelState.update {
                                    it.copy(
                                        isExpectationUpdateLoading = false,
                                        expectationUpdateError = null
                                    )
                                }
                                _navigationEvents.tryEmit(CreateChallengeNavigationEvent.GoBackHome)
                            }
                        }
                    }
                }
            }

            else -> {}
        }
    }

    fun onCommitmentAction(action: CreateChallengeCommitmentAction) {
        when (action) {
            is CreateChallengeCommitmentAction.OnCommitmentInputValueChanged -> {
                val commitment = action.commitment.take(MAX_CHALLENGE_COMMITMENT_LENGTH)
                _createChallengeFunnelState.update {
                    it.copy(
                        commitmentInputValue = commitment,
                    )
                }
            }

            CreateChallengeCommitmentAction.OnFormSubmit -> {
                val form = CreateChallengeForm(
                    name = _createChallengeFunnelState.value.challengeNameInputValue,
                    maxLives = _createChallengeFunnelState.value.numberOfLivesValue,
                    commitment = _createChallengeFunnelState.value.commitmentInputValue,
                    expectations = _createChallengeFunnelState.value.expectationsInputValue
                )

                viewModelScope.launch {
                    challengesService.create(form).collectLatest { result ->
                        when (result) {
                            is Resource.Error -> {
                                _createChallengeFunnelState.update {
                                    it.copy(
                                        isFormSubmissionLoading = false,
                                        formSubmissionError = Res.string.unknown_error,
                                    )
                                }
                            }

                            is Resource.Loading -> {
                                _createChallengeFunnelState.update {
                                    it.copy(
                                        isFormSubmissionLoading = true,
                                        formSubmissionError = null
                                    )
                                }
                            }

                            is Resource.Success -> {
                                _createChallengeFunnelState.update {
                                    it.copy(
                                        isFormSubmissionLoading = false
                                    )
                                }
                                _navigationEvents.tryEmit(CreateChallengeNavigationEvent.GoBackHome)
                            }
                        }
                    }
                }
            }

            is CreateChallengeCommitmentAction.OnUpdateCommitment -> {
                val commitment = _createChallengeFunnelState.value.commitmentInputValue
                viewModelScope.launch {
                    challengesRepository.update(
                        action.challengeId,
                        fields = UpdateChallengeFields(commitment = commitment)
                    ).collectLatest { result ->
                        when (result) {
                            is Resource.Error -> {
                                _createChallengeFunnelState.update {
                                    it.copy(
                                        isFormSubmissionLoading = false,
                                        formSubmissionError = Res.string.unknown_error
                                    )
                                }
                            }

                            is Resource.Loading -> {
                                _createChallengeFunnelState.update {
                                    it.copy(
                                        isFormSubmissionLoading = true,
                                        formSubmissionError = null
                                    )
                                }
                            }

                            is Resource.Success -> {
                                _createChallengeFunnelState.update {
                                    it.copy(isFormSubmissionLoading = false)
                                }
                                _navigationEvents.tryEmit(CreateChallengeNavigationEvent.NavigateUp)
                            }
                        }
                    }
                }
            }

            else -> {}
        }
    }

    private fun requiredFields(name: String): Boolean {
        if (name.isBlank()) {
            _createChallengeFunnelState.update {
                it.copy(
                    challengeNameInputError = Res.string.challenge_info_screen_challenge_title_input_error
                )
            }
        }

        return name.isNotBlank()
    }
}
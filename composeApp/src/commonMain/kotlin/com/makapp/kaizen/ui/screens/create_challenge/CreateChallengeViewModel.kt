package com.makapp.kaizen.ui.screens.create_challenge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.makapp.kaizen.domain.models.CreateChallengeForm
import com.makapp.kaizen.domain.models.Resource
import com.makapp.kaizen.domain.services.ChallengesService
import com.makapp.kaizen.ui.screens.create_challenge.commitment.CreateChallengeCommitmentAction
import com.makapp.kaizen.ui.screens.create_challenge.infos.CreateChallengeInfosAction
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateChallengeViewModel(
    private val challengesService: ChallengesService
) : ViewModel() {
    companion object {
        const val MAX_CHALLENGE_TITLE_LENGTH = 20
        const val MAX_CHALLENGE_COMMITMENT_LENGTH = 150
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

            is CreateChallengeInfosAction.OnNumberOfErrorsInputValueChanged -> {
                val numberOfErrors = action.numberOfErrors
                    .substringBefore(".")
                    .substringBefore(",")
                    .take(MAX_CHALLENGE_ERRORS_LENGTH)

                _createChallengeFunnelState.update {
                    it.copy(
                        numberOfErrorsInputValue = numberOfErrors,
                        numberOfErrorsInputError = null
                    )
                }
            }

            CreateChallengeInfosAction.GoToCommitmentStep -> {
                val name = _createChallengeFunnelState.value.challengeNameInputValue
                val numberOfErrors = _createChallengeFunnelState.value.numberOfErrorsInputValue
                if (!requiredFields(name, numberOfErrors)) {
                    return
                }
                _navigationEvents.tryEmit(CreateChallengeNavigationEvent.GoToCommitmentStep)
            }

            else -> {}
        }
    }

    fun onCommitmentAction(action: CreateChallengeCommitmentAction) {
        when (action) {
            is CreateChallengeCommitmentAction.OnCommitmentInputValueChanged -> {
                _createChallengeFunnelState.update {
                    it.copy(
                        commitmentInputValue = action.commitment,
                    )
                }
            }
            CreateChallengeCommitmentAction.OnFormSubmit -> {
            val form = CreateChallengeForm(
                name = _createChallengeFunnelState.value.challengeNameInputValue,
                numberOfErrors = _createChallengeFunnelState.value.numberOfErrorsInputValue,
                commitment = _createChallengeFunnelState.value.commitmentInputValue
            )

            viewModelScope.launch {
                challengesService.create(form).collectLatest { result ->
                    when (result) {
                        is Resource.Error -> {
                            _createChallengeFunnelState.update {
                                it.copy(
                                    isFormSubmissionLoading = false,
                                    formSubmissionError = result.throwable?.message
                                        ?: "An unknown error has occurred",
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
                            _navigationEvents.tryEmit(CreateChallengeNavigationEvent.GoHome)
                        }
                    }
                }
            }
        }
            else -> {}
        }
    }

    private fun requiredFields(name: String, numberOfErrors: String): Boolean {
        if (name.isBlank()) {
            _createChallengeFunnelState.update {
                it.copy(
                    challengeNameInputError = "Challenge name must not be empty."
                )
            }
        }
        if (numberOfErrors.isBlank()) {
            _createChallengeFunnelState.update {
                it.copy(
                    numberOfErrorsInputError = "Maximum authorized errors must not be empty."
                )
            }
        }

        return name.isNotBlank() && numberOfErrors.isNotBlank()
    }
}
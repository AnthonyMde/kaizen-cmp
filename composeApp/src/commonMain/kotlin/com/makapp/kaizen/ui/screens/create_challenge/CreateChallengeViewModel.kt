package com.makapp.kaizen.ui.screens.create_challenge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.makapp.kaizen.domain.models.Resource
import com.makapp.kaizen.domain.services.ChallengesService

class CreateChallengeViewModel(
    private val challengesService: ChallengesService
) : ViewModel() {
    companion object {
        const val MAX_CHALLENGE_TITLE_LENGTH = 20
        private const val MAX_CHALLENGE_ERRORS_LENGTH = 2
    }

    private val _createChallengeScreenState = MutableStateFlow(CreateChallengeScreenState())
    val createChallengeScreenState = _createChallengeScreenState.asStateFlow()

    private val _navigationEvents = MutableSharedFlow<CreateChallengeNavigationEvent>(
        extraBufferCapacity = 1,
        replay = 0,
        onBufferOverflow = BufferOverflow.DROP_LATEST
    )
    val navigationEvents = _navigationEvents.asSharedFlow()

    fun onAction(action: CreateChallengeAction) {
        when (action) {
            is CreateChallengeAction.OnNameInputValueChanged -> {
                val title = action.title.take(MAX_CHALLENGE_TITLE_LENGTH)
                _createChallengeScreenState.update {
                    it.copy(
                        challengeNameInputValue = title,
                        challengeNameInputError = null
                    )
                }
            }

            is CreateChallengeAction.OnNumberOfErrorsInputValueChanged -> {
                val numberOfErrors = action.numberOfErrors
                    .substringBefore(".")
                    .substringBefore(",")
                    .take(MAX_CHALLENGE_ERRORS_LENGTH)

                _createChallengeScreenState.update {
                    it.copy(
                        numberOfErrorsInputValue = numberOfErrors,
                        numberOfErrorsInputError = null
                    )
                }
            }

            is CreateChallengeAction.OnCreateChallengeFormSubmit -> {
                val name = _createChallengeScreenState.value.challengeNameInputValue
                val numberOfErrors = _createChallengeScreenState.value.numberOfErrorsInputValue

                viewModelScope.launch {
                    if (!requiredFields(name, numberOfErrors)) {
                        return@launch
                    }

                    challengesService.create(
                        name = name,
                        numberOfErrors = numberOfErrors
                    ).collectLatest { result ->
                        when (result) {
                            is Resource.Error -> {
                                _createChallengeScreenState.update {
                                    it.copy(
                                        isFormSubmissionLoading = false,
                                        formSubmissionError = result.throwable?.message
                                            ?: "An unknown error has occurred",
                                    )
                                }
                            }

                            is Resource.Loading -> {
                                _createChallengeScreenState.update {
                                    it.copy(
                                        isFormSubmissionLoading = true,
                                        formSubmissionError = null
                                    )
                                }
                            }

                            is Resource.Success -> {
                                _createChallengeScreenState.update {
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
            _createChallengeScreenState.update {
                it.copy(
                    challengeNameInputError = "Challenge name must not be empty."
                )
            }
        }
        if (numberOfErrors.isBlank()) {
            _createChallengeScreenState.update {
                it.copy(
                    numberOfErrorsInputError = "Maximum authorized errors must not be empty."
                )
            }
        }

        return name.isNotBlank() && numberOfErrors.isNotBlank()
    }
}
package org.towny.kaizen.ui.screens.create_challenge

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CreateChallengeViewModel: ViewModel() {
    companion object {
        const val MAX_CHALLENGE_TITLE_LENGTH = 20
        private const val MAX_CHALLENGE_ERRORS_LENGTH = 2
    }
    private val _createChallengeScreenState = MutableStateFlow(CreateChallengeScreenState())
    val createChallengeScreenState = _createChallengeScreenState.asStateFlow()

    fun onAction(action: CreateChallengeAction) {
        when (action) {
            is CreateChallengeAction.OnTitleInputValueChanged -> {
                val title = action.title.take(MAX_CHALLENGE_TITLE_LENGTH)
                _createChallengeScreenState.update { it.copy(challengeTitleInputValue = title) }
            }
            is CreateChallengeAction.OnNumberOfErrorsInputValueChanged -> {
                val numberOfErrors = action.numberOfErrors
                    .substringBefore(".")
                    .substringBefore(",")
                    .take(MAX_CHALLENGE_ERRORS_LENGTH)

                _createChallengeScreenState.update { it.copy(numberOfErrorsInputValue = numberOfErrors) }
            }
            else -> {}
        }
    }
}
package org.towny.kaizen.ui.screens.create_challenge

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CreateChallengeViewModel: ViewModel() {
    private val _createChallengeScreenState = MutableStateFlow(CreateChallengeScreenState())
    val createChallengeScreenState = _createChallengeScreenState.asStateFlow()

    fun onAction(action: CreateChallengeAction) {
        when (action) {
            else -> {}
        }
    }
}
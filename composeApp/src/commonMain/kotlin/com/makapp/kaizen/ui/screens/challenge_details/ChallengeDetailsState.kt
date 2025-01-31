package com.makapp.kaizen.ui.screens.challenge_details

import com.makapp.kaizen.domain.models.Challenge

data class ChallengeDetailsState(
    val isDetailsLoading: Boolean = true,
    val challenge: Challenge? = null,
    val challengeError: String? = null
)

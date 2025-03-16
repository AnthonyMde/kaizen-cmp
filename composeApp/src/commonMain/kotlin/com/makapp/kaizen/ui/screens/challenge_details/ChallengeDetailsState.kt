package com.makapp.kaizen.ui.screens.challenge_details

import com.makapp.kaizen.domain.models.Challenge

data class ChallengeDetailsState(
    val isDetailsLoading: Boolean = true,
    val title: String = "",
    val challenge: Challenge? = null,
    val challengeError: String? = null,
    val isBottomSheetOpened: Boolean = false,

    val isPauseChallengeModalDisplayed: Boolean = false,
    val isPauseRequestLoading: Boolean = false,

    val isGiveUpChallengeModalDisplayed: Boolean = false,
    val isDeleteChallengeModalDisplayed: Boolean = false,
)

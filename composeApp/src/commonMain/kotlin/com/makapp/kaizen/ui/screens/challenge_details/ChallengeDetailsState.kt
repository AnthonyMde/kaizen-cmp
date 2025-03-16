package com.makapp.kaizen.ui.screens.challenge_details

import com.makapp.kaizen.domain.models.Challenge

data class ChallengeDetailsState(
    val isDetailsLoading: Boolean = true,
    val title: String = "",
    val challenge: Challenge? = null,
    val challengeError: String? = null,
    val isBottomSheetOpened: Boolean = false,

    val isChangeStatusModalDisplayed: Boolean = false,
    val newStatusRequested: Challenge.Status? = null,
    val isChangeStatusRequestLoading: Boolean = false,
    val changeStatusRequestError: String? = null,

    val isDeleteChallengeModalDisplayed: Boolean = false,
    val isDeleteRequestLoading: Boolean = false,
    val deleteRequestError: String? = null
)

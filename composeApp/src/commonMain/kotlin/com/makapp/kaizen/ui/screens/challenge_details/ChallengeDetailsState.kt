package com.makapp.kaizen.ui.screens.challenge_details

import com.makapp.kaizen.domain.models.challenge.Challenge
import org.jetbrains.compose.resources.StringResource

data class ChallengeDetailsState(
    val isDetailsLoading: Boolean = true,
    val title: String = "",
    val challenge: Challenge? = null,
    val challengeError: StringResource? = null,
    val isBottomSheetOpened: Boolean = false,

    val isChangeStatusModalDisplayed: Boolean = false,
    val newStatusRequested: Challenge.Status? = null,
    val isChangeStatusRequestLoading: Boolean = false,
    val changeStatusRequestError: StringResource? = null,

    val isDeleteChallengeModalDisplayed: Boolean = false,
    val isDeleteRequestLoading: Boolean = false,
    val deleteRequestError: StringResource? = null
)

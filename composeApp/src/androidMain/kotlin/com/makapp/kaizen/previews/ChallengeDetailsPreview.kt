package com.makapp.kaizen.previews

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.makapp.kaizen.domain.models.challenge.Challenge
import com.makapp.kaizen.ui.screens.challenge_details.ChallengeDetailsScreen
import com.makapp.kaizen.ui.screens.challenge_details.ChallengeDetailsState
import com.makapp.kaizen.utils.DateUtils

@Preview
@Composable
fun ChallengeDetailsPreview() {
    ChallengeDetailsScreen(
        isEditable = true,
        state = ChallengeDetailsState(
            isDetailsLoading = false,
            challenge = Challenge(
                id = "1",
                name = "Écriture",
                status = Challenge.Status.ON_GOING,
                createdAt = DateUtils.getCurrentLocalDate(),
                updatedAt = DateUtils.getCurrentLocalDate(),
                days = 150,
                isDoneForToday = true,
                failureCount = 2,
                maxAuthorizedFailures = 4,
                commitment = "I must write one line each day",
                expectations = "I want writing to be part of my life.",
                lastFailureDate = null,
                didUseForgotFeatureToday = false,
            ),
            challengeError = null
        ),
        onAction = {}
    )
}

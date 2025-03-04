package com.makapp.kaizen.previews

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.makapp.kaizen.domain.models.Challenge
import com.makapp.kaizen.ui.screens.challenge_details.ChallengeDetailsNavArgs
import com.makapp.kaizen.ui.screens.challenge_details.ChallengeDetailsScreen
import com.makapp.kaizen.ui.screens.challenge_details.ChallengeDetailsState
import com.makapp.kaizen.utils.DateUtils

@Preview
@Composable
fun ChallengeDetailsPreview() {
    ChallengeDetailsScreen(
        navArgs = ChallengeDetailsNavArgs(
            id = "1",
            title = "Écriture",
            isDone = true,
        ),
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
                maxAuthorizedFailures = 4
            ),
            challengeError = null
        ),
        getChallengeStatusName = { status ->
            status.name
        },
        onAction = {}
    )
}

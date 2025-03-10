package com.makapp.kaizen.previews

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.makapp.kaizen.domain.models.Challenge
import com.makapp.kaizen.domain.models.Friend
import com.makapp.kaizen.domain.models.User
import com.makapp.kaizen.ui.screens.home.HomeScreen
import com.makapp.kaizen.ui.screens.home.HomeScreenState
import com.makapp.kaizen.utils.DateUtils

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        state = HomeScreenState(
            currentChallenger = User(
                id = "1",
                email = "email@tony.com",
                name = "towny",
                displayName = "Towny",
                profilePictureIndex = 0,
                challenges = listOf(
                    Challenge(
                        id= "1",
                        name = "Writing",
                        maxAuthorizedFailures = 0,
                        failureCount = 0,
                        createdAt = DateUtils.getCurrentLocalDate(),
                        updatedAt = DateUtils.getCurrentLocalDate(),
                        isDoneForToday = true,
                        status = Challenge.Status.ON_GOING,
                        days = 23,
                        commitment = null,
                        expectations = null
                    ),
                    Challenge(
                        id= "2",
                        name = "Programming",
                        maxAuthorizedFailures = 0,
                        failureCount = 0,
                        createdAt = DateUtils.getCurrentLocalDate(),
                        updatedAt = DateUtils.getCurrentLocalDate(),
                        isDoneForToday = true,
                        status = Challenge.Status.ON_GOING,
                        days = 54,
                        commitment = null,
                        expectations = null
                    )
                )
            ),
            friends = listOf(
                Friend(
                    id = "2",
                    name = "clowie",
                    displayName = "Clowie",
                    profilePictureIndex = 1,
                    isFavorite = true,
                    challenges = listOf(
                        Challenge(
                            id= "1",
                            name = "Reading",
                            maxAuthorizedFailures = 0,
                            failureCount = 0,
                            createdAt = DateUtils.getCurrentLocalDate(),
                            updatedAt = DateUtils.getCurrentLocalDate(),
                            isDoneForToday = true,
                            status = Challenge.Status.ON_GOING,
                            days = 54,
                            commitment = null,
                            expectations = null
                        ),
                        Challenge(
                            id= "2",
                            name = "Fitness",
                            maxAuthorizedFailures = 3,
                            failureCount = 0,
                            createdAt = DateUtils.getCurrentLocalDate(),
                            updatedAt = DateUtils.getCurrentLocalDate(),
                            isDoneForToday = true,
                            status = Challenge.Status.ON_GOING,
                            days = 54,
                            commitment = null,
                            expectations = null
                        )
                    )
                ),
                Friend(
                    id = "3",
                    name = "j",
                    displayName = "Jacques",
                    profilePictureIndex = 2,
                    isFavorite = true,
                    challenges = listOf(
                        Challenge(
                            id= "1",
                            name = "Dance",
                            maxAuthorizedFailures = 0,
                            failureCount = 0,
                            createdAt = DateUtils.getCurrentLocalDate(),
                            updatedAt = DateUtils.getCurrentLocalDate(),
                            isDoneForToday = true,
                            status = Challenge.Status.ON_GOING,
                            days = 54,
                            commitment = null,
                            expectations = null
                        ),
                        Challenge(
                            id= "2",
                            name = "Side projects",
                            maxAuthorizedFailures = 0,
                            failureCount = 0,
                            createdAt = DateUtils.getCurrentLocalDate(),
                            updatedAt = DateUtils.getCurrentLocalDate(),
                            isDoneForToday = true,
                            status = Challenge.Status.ON_GOING,
                            days = 54,
                            commitment = null,
                            expectations = null
                        )
                    )
                )
            )
        ),
        onAction = {}
    )
}
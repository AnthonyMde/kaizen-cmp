package org.towny.kaizen.previews

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.towny.kaizen.domain.models.Challenge
import org.towny.kaizen.domain.models.User
import org.towny.kaizen.ui.screens.home.HomeScreen
import org.towny.kaizen.ui.screens.home.HomeScreenState
import org.towny.kaizen.utils.DateUtils

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        state = HomeScreenState(
            currentChallenger = User(
                id = "1",
                name = "Towny",
                challenges = listOf(
                    Challenge(
                        id= "1",
                        name = "Writing",
                        maxFailures = 0,
                        failures = 0,
                        createdAt = DateUtils.getCurrentLocalDate(),
                        isCompleted = true
                    ),
                    Challenge(
                        id= "2",
                        name = "Programming",
                        maxFailures = 0,
                        failures = 0,
                        createdAt = DateUtils.getCurrentLocalDate(),
                        isCompleted = true
                    )
                )
            ),
            otherChallengers = listOf(
                User(
                    id = "2",
                    name = "Clowie",
                    challenges = listOf(
                        Challenge(
                            id= "1",
                            name = "Reading",
                            maxFailures = 0,
                            failures = 0,
                            createdAt = DateUtils.getCurrentLocalDate(),
                            isCompleted = true
                        ),
                        Challenge(
                            id= "2",
                            name = "Fitness",
                            maxFailures = 3,
                            failures = 0,
                            createdAt = DateUtils.getCurrentLocalDate(),
                            isCompleted = true
                        )
                    )
                ),
                User(
                    id = "3",
                    name = "Jacques",
                    challenges = listOf(
                        Challenge(
                            id= "1",
                            name = "Dance",
                            maxFailures = 0,
                            failures = 0,
                            createdAt = DateUtils.getCurrentLocalDate(),
                            isCompleted = true
                        ),
                        Challenge(
                            id= "2",
                            name = "Side projects",
                            maxFailures = 0,
                            failures = 0,
                            createdAt = DateUtils.getCurrentLocalDate(),
                            isCompleted = true
                        )
                    )
                )
            )
        ),
        onAction = {}
    )
}
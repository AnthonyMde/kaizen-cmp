package org.towny.kaizen.domain.entities

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val name: String,
    val challenges: List<Challenge>,
    val isWasted: Boolean
) {
    companion object {
        fun getMockedChallengers(): List<User> {
            return listOf(
                User(
                    id = "2",
                    name = "Clowie",
                    challenges = listOf(
                        Challenge(
                            id = "1", name = "Reading", failures = 0, maxFailures = 0, isCompleted = true
                        ),
                        Challenge(
                            id = "2", name = "Fitness", failures = 0, maxFailures = 3, isCompleted = false
                        )
                    ),
                    isWasted = false
                ),
                User(
                    id = "2",
                    name = "Jack",
                    challenges = listOf(
                        Challenge(
                            id = "1", name = "Dance", failures = 0, maxFailures = 0, isCompleted = false
                        ),
                        Challenge(
                            id = "2", name = "Side projects", failures = 0, maxFailures = 0, isCompleted = true
                        )
                    ),
                    isWasted = false
                )
            )
        }

        fun getMockedUser(): User {
            return User(
                id = "1",
                name = "Towny",
                challenges = listOf(
                    Challenge(
                        id = "1", name = "Writing", failures = 0, maxFailures = 0, isCompleted = false
                    ),
                    Challenge(
                        id = "2", name = "Programming", failures = 0, maxFailures = 0, isCompleted = false
                    )
                ),
                isWasted = false
            )
        }
    }
}
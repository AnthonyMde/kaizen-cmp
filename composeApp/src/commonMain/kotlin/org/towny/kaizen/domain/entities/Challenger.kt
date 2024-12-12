package org.towny.kaizen.domain.entities

data class Challenger(
    val id: String,
    val name: String,
    val challenges: List<Challenge>,
    val isWasted: Boolean
) {
    companion object {
        fun getMockedChallengers(): List<Challenger> {
            return listOf(
                Challenger(
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
                Challenger(
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

        fun getMockedUser(): Challenger {
            return Challenger(
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
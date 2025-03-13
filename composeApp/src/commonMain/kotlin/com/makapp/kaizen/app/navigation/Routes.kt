package com.makapp.kaizen.app.navigation

import kotlinx.serialization.Serializable

sealed class Route {
    @Serializable
    data object Home : Route()

    @Serializable
    data object Login : Route()

    @Serializable
    data object OnboardingProfile : Route()

    @Serializable
    data object Account : Route()

    @Serializable
    data object MyFriends : Route()

    @Serializable
    data object CreateChallengeGraph : Route()

    @Serializable
    data object CreateChallengeInfosStep : Route()

    @Serializable
    data class CreateChallengeExpectationsStep(
        val editing: Boolean,
        val expectations: String?,
        val challengeId: String?
    ) : Route()

    @Serializable
    data class CreateChallengeCommitmentStep(val editing: Boolean, val challengeId: String?) :
        Route()

    @Serializable
    data class ChallengeDetails(
        val id: String,
        val title: String,
        val isDone: Boolean,
        val readOnly: Boolean
    ) : Route()
}
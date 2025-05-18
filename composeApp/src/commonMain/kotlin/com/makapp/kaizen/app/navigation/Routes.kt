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
    data object Profile : Route()

    @Serializable
    data class CreateChallengeInfosStep(
        val editing: Boolean,
        val title: String? = null,
        val lives: Int? = null,
        val challengeId: String? = null
    ) : Route()

    @Serializable
    data class CreateChallengeExpectationsStep(
        val editing: Boolean,
        val expectations: String? = null,
        val challengeId: String? = null
    ) : Route()

    @Serializable
    data class CreateChallengeCommitmentStep(
        val editing: Boolean,
        val commitment: String? = null,
        val challengeId: String? = null
    ) : Route()

    @Serializable
    data class ChallengeDetails(
        val id: String,
        val readOnly: Boolean
    ) : Route()
}
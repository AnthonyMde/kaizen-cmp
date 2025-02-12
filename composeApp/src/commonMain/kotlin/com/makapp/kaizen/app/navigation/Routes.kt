package com.makapp.kaizen.app.navigation

import kotlinx.serialization.Serializable

sealed class Route {
    @Serializable
    data object HomeGraph : Route()
    @Serializable
    data object Home : Route()

    @Serializable
    data object AuthenticationGraph : Route()
    @Serializable
    data object Login : Route()

    @Serializable
    data object OnboardingGraph : Route()
    @Serializable
    data object OnboardingProfile : Route()

    @Serializable
    data object AccountGraph : Route()
    @Serializable
    data object Account : Route()

    @Serializable
    data object MyFriends : Route()

    @Serializable
    data object CreateChallenge : Route()
    @Serializable
    data class ChallengeDetails(val id: String, val title: String, val isDone: Boolean) : Route()
}
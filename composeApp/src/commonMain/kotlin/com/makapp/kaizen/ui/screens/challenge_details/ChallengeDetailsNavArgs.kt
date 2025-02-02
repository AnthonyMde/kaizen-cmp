package com.makapp.kaizen.ui.screens.challenge_details

import kotlinx.serialization.Serializable

@Serializable
data class ChallengeDetailsNavArgs(
    val id: String,
    val title: String,
    val isDone: Boolean
)

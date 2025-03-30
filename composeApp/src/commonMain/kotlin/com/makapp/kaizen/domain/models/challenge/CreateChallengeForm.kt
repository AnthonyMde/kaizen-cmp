package com.makapp.kaizen.domain.models.challenge

data class CreateChallengeForm(
    val name: String,
    val maxLives: Int,
    val expectations: String?,
    val commitment: String?
)

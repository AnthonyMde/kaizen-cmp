package com.makapp.kaizen.domain.models

data class CreateChallengeForm(
    val name: String,
    val maxLives: Int,
    val expectations: String?,
    val commitment: String?
)

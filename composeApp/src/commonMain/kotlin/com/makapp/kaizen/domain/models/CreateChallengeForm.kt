package com.makapp.kaizen.domain.models

data class CreateChallengeForm(
    val name: String,
    val numberOfErrors: String,
    val commitment: String?
)

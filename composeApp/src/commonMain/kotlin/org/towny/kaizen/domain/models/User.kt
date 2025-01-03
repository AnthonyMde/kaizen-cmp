package org.towny.kaizen.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val name: String,
    val challenges: List<Challenge>
)
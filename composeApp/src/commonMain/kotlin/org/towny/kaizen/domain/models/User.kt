package org.towny.kaizen.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val email: String,
    val name: String,
    val profilePictureIndex: Int,
    val challenges: List<Challenge>
)
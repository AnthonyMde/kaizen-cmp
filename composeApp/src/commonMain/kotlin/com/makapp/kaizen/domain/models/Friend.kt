package com.makapp.kaizen.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Friend(
    val id: String,
    val name: String,
    val displayName: String? = null,
    val profilePictureIndex: Int,
    val challenges: List<Challenge>,
    val isFavorite: Boolean
)

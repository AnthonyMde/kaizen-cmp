package com.makapp.kaizen.domain.models.friend

import com.makapp.kaizen.domain.models.challenge.Challenge
import kotlinx.serialization.Serializable

@Serializable
data class Friend(
    val id: String,
    val name: String,
    val displayName: String? = null,
    val profilePictureIndex: Int,
    val challenges: List<Challenge>,
    val isFavorite: Boolean
) {
    fun getUsername() = if (!displayName.isNullOrBlank())
        displayName
    else
        name
}

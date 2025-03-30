package com.makapp.kaizen.domain.models.friend

import kotlinx.serialization.Serializable

@Serializable
data class FriendPreview(
    val id: String,
    val name: String,
    val displayName: String? = null,
    val profilePictureIndex: Int,
    val isFavorite: Boolean
) {
    fun getUsername() = if (!displayName.isNullOrBlank())
        displayName
    else
        name
}

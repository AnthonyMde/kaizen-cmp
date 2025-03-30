package com.makapp.kaizen.domain.models.friend

import kotlinx.serialization.Serializable

@Serializable
data class FriendSearchPreview(
    val id: String,
    val name: String,
    val displayName: String? = null,
    val profilePictureIndex: Int
)

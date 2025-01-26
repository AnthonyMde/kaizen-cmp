package com.makapp.kaizen.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class FriendPreview(
    val id: String,
    val name: String,
    val displayName: String,
    val profilePictureIndex: Int
)

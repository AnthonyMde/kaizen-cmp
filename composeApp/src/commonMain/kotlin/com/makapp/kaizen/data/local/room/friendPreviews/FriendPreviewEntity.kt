package com.makapp.kaizen.data.local.room.friendPreviews

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FriendPreviewEntity(
    @PrimaryKey val id: String,
    val name: String,
    val displayName: String,
    val profilePictureIndex: Int,
    val isFavorite: Boolean
)

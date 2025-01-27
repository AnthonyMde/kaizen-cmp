package com.makapp.kaizen.data.local.room.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserEntity(
    @PrimaryKey val id: String,
    val email: String,
    val name: String,
    val displayName: String? = null,
    val profilePictureIndex: Int,
    val friendIds: List<String>
)

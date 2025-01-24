package com.makapp.kaizen.data.local.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FriendRequestEntity(
    @PrimaryKey val id: String,
    val sender: String,
    val receiver: String,
    val status: String
)

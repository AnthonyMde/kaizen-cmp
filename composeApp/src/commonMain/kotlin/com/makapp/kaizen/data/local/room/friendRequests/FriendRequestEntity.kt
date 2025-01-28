package com.makapp.kaizen.data.local.room.friendRequests

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.makapp.kaizen.domain.models.FriendRequest
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class FriendRequestEntity(
    @PrimaryKey val id: String,
    val senderId: String,
    val receiverId: String,
    val status: FriendRequest.Status
)

@Serializable
@Entity
data class FriendRequestProfileEntity(
    @PrimaryKey val id: String,
    val username: String,
    val displayName: String? = null,
    val profilePictureIndex: Int? = null
)

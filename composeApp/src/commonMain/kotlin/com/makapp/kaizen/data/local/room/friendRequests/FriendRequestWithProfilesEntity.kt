package com.makapp.kaizen.data.local.room.friendRequests

import androidx.room.Embedded
import androidx.room.Relation


data class FriendRequestWithProfilesEntity(
    @Embedded
    val friendRequest: FriendRequestEntity,
    @Relation(
        parentColumn = "senderId",
        entityColumn = "id"
    )
    val sender: FriendRequestProfileEntity,
    @Relation(
        parentColumn = "receiverId",
        entityColumn = "id"
    )
    val receiver: FriendRequestProfileEntity
)

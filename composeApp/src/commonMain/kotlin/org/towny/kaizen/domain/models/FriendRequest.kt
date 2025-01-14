package org.towny.kaizen.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class FriendRequest(
    val id: String,
    val sender: FriendRequestProfile,
    val receiver: FriendRequestProfile,
    val status: Status
) {
    @Serializable
    enum class Status {
        PENDING, DECLINED, ACCEPTED, CANCELED
    }
}

@Serializable
data class FriendRequestProfile(
    val id: String,
    val username: String,
    val profilePictureIndex: Int? = null
)

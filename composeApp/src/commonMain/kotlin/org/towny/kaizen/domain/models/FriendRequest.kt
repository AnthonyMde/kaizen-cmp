package org.towny.kaizen.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class FriendRequest(
    val sender: FriendRequestProfile,
    val receiver: FriendRequestProfile,
    val status: Status
) {
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

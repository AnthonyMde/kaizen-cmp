package org.towny.kaizen.domain.models

data class FriendRequest(
    val sender: FriendRequestProfile,
    val receiver: FriendRequestProfile,
    val state: State
) {
    enum class State {
        PENDING, DECLINED, ACCEPTED, CANCELED
    }
}

data class FriendRequestProfile(
    val username: String,
    val profilePictureIndex: Int? = null
)

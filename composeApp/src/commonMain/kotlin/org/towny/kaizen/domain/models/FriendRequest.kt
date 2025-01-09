package org.towny.kaizen.domain.models

data class FriendRequest(
    val sendTo: String,
    val from: String,
    val state: FriendRequestState
) {
    enum class FriendRequestState {
        PENDING, DECLINED, ACCEPTED
    }
}

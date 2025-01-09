package org.towny.kaizen.ui.screens.my_friends.models

import org.towny.kaizen.domain.models.FriendRequest

data class FriendRequestUI(
    val state: FriendRequest.State,
    val username: String,
    val profilePictureIndex: Int? = null
)

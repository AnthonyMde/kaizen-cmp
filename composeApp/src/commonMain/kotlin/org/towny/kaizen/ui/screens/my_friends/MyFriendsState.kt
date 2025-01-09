package org.towny.kaizen.ui.screens.my_friends

import org.towny.kaizen.ui.screens.my_friends.models.FriendRequestUI

data class MyFriendsState(
    val friendUsernameInputValue: String = "",
    val friendUsernameInputError: String? = null,
    val isFormSubmissionLoading: Boolean = false,

    val pendingSentRequests: List<FriendRequestUI> = emptyList(),
    val pendingReceivedRequests: List<FriendRequestUI> = emptyList()
)

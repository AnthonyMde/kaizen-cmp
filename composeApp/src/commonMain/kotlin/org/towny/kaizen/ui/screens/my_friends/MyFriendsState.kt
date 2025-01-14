package org.towny.kaizen.ui.screens.my_friends

import org.towny.kaizen.domain.models.FriendPreview
import org.towny.kaizen.domain.models.FriendRequest

data class MyFriendsState(
    val friendUsernameInputValue: String = "",
    val friendUsernameInputError: String? = null,
    val isFriendPreviewLoading: Boolean = false,
    val friendPreview: FriendPreview? = null,
    val isSendFriendRequestLoading: Boolean = false,

    val pendingSentRequests: List<FriendRequest> = emptyList(),
    val pendingReceivedRequests: List<FriendRequest> = emptyList(),
    val areFriendRequestsLoading: Boolean = true
) {
    val totalRequests: Int
        get() = pendingReceivedRequests.size + pendingSentRequests.size
}

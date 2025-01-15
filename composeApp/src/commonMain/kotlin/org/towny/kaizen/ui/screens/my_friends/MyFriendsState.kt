package org.towny.kaizen.ui.screens.my_friends

import org.towny.kaizen.domain.models.Friend
import org.towny.kaizen.domain.models.FriendPreview
import org.towny.kaizen.domain.models.FriendRequest

data class MyFriendsState(
    val friendUsernameInputValue: String = "",
    val friendUsernameInputError: String? = null,

    val isFriendPreviewLoading: Boolean = false,
    val friendPreview: FriendPreview? = null,
    val isSendFriendRequestLoading: Boolean = false,

    val areFriendRequestsLoading: Boolean = true,
    val pendingSentRequests: List<FriendRequest> = emptyList(),
    val pendingReceivedRequests: List<FriendRequest> = emptyList(),
    val requestIdsCurrentlyUpdated: List<String> = emptyList(),

    val friends: List<Friend> = emptyList(),
    val isFriendsLoading: Boolean = false
) {
    val totalRequests: Int
        get() = pendingReceivedRequests.size + pendingSentRequests.size
}

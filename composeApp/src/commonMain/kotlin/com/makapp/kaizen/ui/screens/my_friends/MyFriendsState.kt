package com.makapp.kaizen.ui.screens.my_friends

import com.makapp.kaizen.domain.models.FriendPreview
import com.makapp.kaizen.domain.models.FriendRequest

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

    val areFriendPreviewsLoading: Boolean = false,
    val friendPreviews: List<FriendPreview> = emptyList()
) {
    val totalRequests: Int
        get() = pendingReceivedRequests.size + pendingSentRequests.size
}

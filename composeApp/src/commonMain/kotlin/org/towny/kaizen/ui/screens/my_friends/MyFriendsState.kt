package org.towny.kaizen.ui.screens.my_friends

import org.towny.kaizen.domain.models.FriendPreview
import org.towny.kaizen.domain.models.FriendRequest

data class MyFriendsState(
    val friendUsernameInputValue: String = "",
    val friendUsernameInputError: String? = null,
    val isFriendPreviewLoading: Boolean = false,
    val friendPreview: FriendPreview? = null,

    val pendingSentRequests: List<FriendRequest> = emptyList(),
    val pendingReceivedRequests: List<FriendRequest> = emptyList()
)

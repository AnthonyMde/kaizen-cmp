package com.makapp.kaizen.ui.screens.my_friends

import com.makapp.kaizen.domain.models.FriendPreview
import com.makapp.kaizen.domain.models.FriendRequest
import com.makapp.kaizen.domain.models.FriendSearchPreview
import org.jetbrains.compose.resources.StringResource

data class MyFriendsState(
    // Search friend
    val friendUsernameInputValue: String = "",
    val friendUsernameInputError: StringResource? = null,

    // Search Friend Preview
    val isFriendPreviewLoading: Boolean = false,
    val friendSearchPreview: FriendSearchPreview? = null,
    val isSendFriendRequestLoading: Boolean = false,

    // Friend requests
    val pendingSentRequests: List<FriendRequest> = emptyList(),
    val pendingReceivedRequests: List<FriendRequest> = emptyList(),
    val requestIdsUnderUpdate: List<String> = emptyList(),

    // Friends
    val friendPreviews: List<FriendPreview> = emptyList(),
    val friendIdsUnderUpdate: List<String> = emptyList()
) {
    val totalRequests: Int
        get() = pendingReceivedRequests.size + pendingSentRequests.size
}

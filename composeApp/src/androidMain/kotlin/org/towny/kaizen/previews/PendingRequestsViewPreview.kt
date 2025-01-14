package org.towny.kaizen.previews

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.towny.kaizen.domain.models.FriendRequest
import org.towny.kaizen.domain.models.FriendRequestProfile
import org.towny.kaizen.ui.screens.my_friends.components.PendingRequestsView

@Composable
@Preview
fun PendingRequestsViewPreview() {
    PendingRequestsView(
        sentRequests = sentRequests,
        receivedRequests = receivedRequests
    )
}

private val sentRequests = listOf(
    FriendRequest(
        id = "1",
        sender = FriendRequestProfile(
            id = "1",
            username = "Towny",
            profilePictureIndex = 0
        ),
        receiver = FriendRequestProfile(
            id = "2",
            username = "Clowie",
            profilePictureIndex = 1
        ),
        FriendRequest.Status.PENDING
    ),
    FriendRequest(
        id = "1",
        sender = FriendRequestProfile(
            id = "1",
            username = "Towny",
            profilePictureIndex = 0
        ),
        receiver = FriendRequestProfile(
            id = "3",
            username = "Jacques",
            profilePictureIndex = 2
        ),
        FriendRequest.Status.PENDING
    )
)

private val receivedRequests = listOf(
    FriendRequest(
        id = "1",
        receiver = FriendRequestProfile(
            id = "1",
            username = "Towny",
            profilePictureIndex = 0
        ),
        sender = FriendRequestProfile(
            id = "2",
            username = "Clowie",
            profilePictureIndex = 1
        ),
        status = FriendRequest.Status.PENDING
    ),
    FriendRequest(
        id = "1",
        receiver = FriendRequestProfile(
            id = "1",
            username = "Towny",
            profilePictureIndex = 0
        ),
        sender = FriendRequestProfile(
            id = "3",
            username = "Jacques",
            profilePictureIndex = 2
        ),
        status = FriendRequest.Status.PENDING
    )
)

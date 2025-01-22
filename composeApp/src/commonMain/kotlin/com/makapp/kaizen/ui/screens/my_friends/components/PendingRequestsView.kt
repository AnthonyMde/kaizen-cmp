package com.makapp.kaizen.ui.screens.my_friends.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.makapp.kaizen.domain.models.FriendRequest
import com.makapp.kaizen.ui.screens.my_friends.MyFriendsAction

@Composable
fun PendingRequestsView(
    sentRequests: List<FriendRequest>,
    receivedRequests: List<FriendRequest>,
    requestIdsCurrentlyUpdated: List<String>,
    onAction: (MyFriendsAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        receivedRequests.forEach { request ->
            val isLoading = requestIdsCurrentlyUpdated.contains(request.id)
            ReceivedFriendRequestView(
                request = request,
                isUpdateRequestLoading = isLoading,
                onAction = onAction
            )
        }

        if (sentRequests.isNotEmpty() && receivedRequests.isNotEmpty()) {
            HorizontalDivider(
                modifier = Modifier
                    .width(120.dp)
            )
        }

        sentRequests.forEach { request ->
            val isLoading = requestIdsCurrentlyUpdated.contains(request.id)
            SentFriendRequestView(
                request = request,
                isUpdateRequestLoading = isLoading,
                onAction = onAction
            )
        }
    }
}
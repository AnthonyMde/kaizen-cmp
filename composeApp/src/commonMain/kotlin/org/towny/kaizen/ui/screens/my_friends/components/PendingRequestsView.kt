package org.towny.kaizen.ui.screens.my_friends.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.towny.kaizen.domain.models.FriendRequest

@Composable
fun PendingRequestsView(
    sentRequests: List<FriendRequest>,
    receivedRequests: List<FriendRequest>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        receivedRequests.forEach { request ->
            ReceivedFriendRequestView(request = request)
        }

        if (sentRequests.isNotEmpty() && receivedRequests.isNotEmpty()) {
            HorizontalDivider(modifier = Modifier
                .width(120.dp))
        }

        sentRequests.forEach { request ->
            SentFriendRequestView(request = request)
        }
    }
}
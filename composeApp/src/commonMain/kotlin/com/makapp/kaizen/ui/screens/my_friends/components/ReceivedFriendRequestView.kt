package com.makapp.kaizen.ui.screens.my_friends.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.makapp.kaizen.domain.models.friend.FriendRequest
import com.makapp.kaizen.ui.resources.avatars
import com.makapp.kaizen.ui.screens.my_friends.MyFriendsAction
import kaizen.composeapp.generated.resources.Res
import kaizen.composeapp.generated.resources.friends_request_accept_button
import kaizen.composeapp.generated.resources.friends_request_decline_button_description
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ReceivedFriendRequestView(
    request: FriendRequest,
    isUpdateRequestLoading: Boolean,
    onAction: (MyFriendsAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        request.sender.profilePictureIndex?.let { index ->
            Image(
                painter = painterResource(avatars[index].drawable),
                contentDescription = stringResource(avatars[index].description),
                modifier = Modifier
                    .size(44.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                request.sender.getName(),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            if (isUpdateRequestLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
            } else {
                RequestActionButtons(request.id, onAction)
            }
        }
    }
}

@Composable
private fun RequestActionButtons(
    requestId: String,
    onAction: (MyFriendsAction) -> Unit
) {
    OutlinedButton(
        onClick = {
            onAction(
                MyFriendsAction.OnFriendRequestUpdated(
                    requestId,
                    FriendRequest.Status.ACCEPTED
                )
            )
        },
        content = {
            Text(
                text = stringResource(Res.string.friends_request_accept_button),
                style = MaterialTheme.typography.bodyMedium.copy()
            )
        },
        modifier = Modifier.height(28.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    )

    Spacer(modifier = Modifier.width(8.dp))

    IconButton(
        onClick = {
            onAction(
                MyFriendsAction.OnFriendRequestUpdated(
                    requestId,
                    FriendRequest.Status.DECLINED
                )
            )
        },
        content = {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(Res.string.friends_request_decline_button_description),
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        },
        modifier = Modifier
            .size(18.dp)
    )
}

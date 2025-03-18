package com.makapp.kaizen.ui.screens.my_friends.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import com.makapp.kaizen.domain.models.FriendRequest
import com.makapp.kaizen.ui.resources.avatars
import com.makapp.kaizen.ui.screens.my_friends.MyFriendsAction
import kaizen.composeapp.generated.resources.Res
import kaizen.composeapp.generated.resources.cancel_icon
import kaizen.composeapp.generated.resources.friends_request_cancel_request_description
import kaizen.composeapp.generated.resources.friends_request_pending_request
import org.jetbrains.compose.resources.stringResource

@Composable
fun SentFriendRequestView(
    request: FriendRequest,
    isUpdateRequestLoading: Boolean,
    onAction: (MyFriendsAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Box {
        Row(
            modifier = modifier
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            request.receiver.profilePictureIndex?.let { index ->
                Image(
                    painter = painterResource(avatars[index].drawable),
                    contentDescription = stringResource(avatars[index].description),
                    modifier = Modifier
                        .size(44.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = stringResource(Res.string.friends_request_pending_request, request.receiver.getName()),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 24.dp),
                )
            }
        }
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    color = Color.Gray.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(end = 16.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            if (isUpdateRequestLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(24.dp),
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                    strokeWidth = 2.dp
                )
            } else {
                RequestActionButton(request.id, onAction)
            }
        }
    }
}

@Composable
private fun RequestActionButton(
    requestId: String,
    onAction: (MyFriendsAction) -> Unit
) {
    IconButton(
        onClick = {
            onAction(
                MyFriendsAction.OnFriendRequestUpdated(
                    requestId,
                    FriendRequest.Status.CANCELED
                )
            )
        },
        content = {
            Icon(
                painter = painterResource(Res.drawable.cancel_icon),
                contentDescription = stringResource(Res.string.friends_request_cancel_request_description),
                tint = MaterialTheme.colorScheme.secondary,
            )
        },
        modifier = Modifier
            .size(24.dp)
    )
}

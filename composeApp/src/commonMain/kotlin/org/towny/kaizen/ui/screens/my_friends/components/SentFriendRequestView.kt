package org.towny.kaizen.ui.screens.my_friends.components

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
import org.towny.kaizen.domain.models.FriendRequest
import org.towny.kaizen.ui.resources.avatars

@Composable
fun SentFriendRequestView(
    request: FriendRequest,
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
                    contentDescription = avatars[index].description,
                    modifier = Modifier
                        .size(44.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Waiting ${request.receiver.username}'s answer",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        Box(
            modifier = Modifier.matchParentSize().background(
                color = Color.Gray.copy(alpha = 0.5f),
                shape = RoundedCornerShape(16.dp)
            ).padding(end = 16.dp)
        ) {
            IconButton(
                onClick = {},
                content = {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cancel friend request",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                },
                modifier = Modifier
                    .size(18.dp)
                    .align(Alignment.CenterEnd)
            )
        }
    }
}

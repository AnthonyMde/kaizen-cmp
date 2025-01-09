package org.towny.kaizen.ui.screens.my_friends.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.towny.kaizen.ui.resources.avatars
import org.towny.kaizen.ui.screens.my_friends.models.FriendRequestUI

@Composable
fun ReceivedFriendRequestView(
    request: FriendRequestUI,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .background(color = MaterialTheme.colorScheme.primaryContainer)
    ) {
        request.profilePictureIndex?.let { index ->
            Image(
                painter = painterResource(avatars[index].drawable),
                contentDescription = avatars[index].description
            )
            Text(request.username,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Accept friend request",
                tint = MaterialTheme.colorScheme.tertiaryContainer,
                modifier = Modifier
                    .size(24.dp)
            )
        }
    }
}

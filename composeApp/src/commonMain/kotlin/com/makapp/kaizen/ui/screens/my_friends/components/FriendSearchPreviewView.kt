package com.makapp.kaizen.ui.screens.my_friends.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.makapp.kaizen.domain.models.friend.FriendSearchPreview
import com.makapp.kaizen.ui.resources.avatars
import com.makapp.kaizen.ui.screens.my_friends.MyFriendsAction
import kaizen.composeapp.generated.resources.Res
import kaizen.composeapp.generated.resources.friends_search_preview_send_friend_request_description
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun FriendSearchPreviewView(
    friend: FriendSearchPreview,
    isLoading: Boolean,
    onAction: (MyFriendsAction) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .clip(
                shape = RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 0.dp,
                    bottomStart = 16.dp,
                    bottomEnd = 16.dp
                ),
            )
            .background(
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
            )
            .padding(top = 16.dp)
            .padding(start = 16.dp, end = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(avatars[friend.profilePictureIndex].drawable),
            contentDescription = null,
            modifier = Modifier.size(44.dp)
        )
        Text(friend.displayName ?: friend.name,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
            overflow = TextOverflow.Ellipsis,
            maxLines = 2,
            modifier = Modifier.weight(1f)
        )

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = (2.dp))
            Spacer(modifier = Modifier.width(0.dp)) // Hack to avoid miscalculation with IconButton.
        } else {
            IconButton(
                onClick = {
                    onAction(MyFriendsAction.OnFriendRequestSubmit)
                },
                content = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = stringResource(Res.string.friends_search_preview_send_friend_request_description),
                        modifier = Modifier.size(24.dp)
                    )
                }
            )
        }
    }
}

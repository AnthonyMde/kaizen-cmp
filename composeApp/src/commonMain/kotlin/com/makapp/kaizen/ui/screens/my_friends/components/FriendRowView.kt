package com.makapp.kaizen.ui.screens.my_friends.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.makapp.kaizen.domain.models.friend.FriendPreview
import com.makapp.kaizen.ui.resources.avatars
import com.makapp.kaizen.ui.screens.my_friends.MyFriendsAction
import kaizen.composeapp.generated.resources.Res
import kaizen.composeapp.generated.resources.filled_star_icon
import kaizen.composeapp.generated.resources.friends_screen_add_as_favorite_description
import kaizen.composeapp.generated.resources.friends_screen_remove_as_favorite_description
import kaizen.composeapp.generated.resources.outlined_star_icon
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun FriendRowView(
    friend: FriendPreview,
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    onAction: (MyFriendsAction) -> Unit
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
        friend.profilePictureIndex.let { index ->
            Image(
                painter = painterResource(avatars[index].drawable),
                contentDescription = stringResource(avatars[index].description),
                modifier = Modifier
                    .size(44.dp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            friend.getUsername(),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )

        IconButton(
            onClick = {
                onAction(MyFriendsAction.OnToggleFriendAsFavorite(friend.id))
            },
            content = {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp,
                        modifier = Modifier
                            .size(20.dp)
                    )
                } else {
                    Icon(
                        painter = if (friend.isFavorite) painterResource(Res.drawable.filled_star_icon)
                        else painterResource(Res.drawable.outlined_star_icon),
                        contentDescription = if (friend.isFavorite)
                            stringResource(Res.string.friends_screen_remove_as_favorite_description)
                        else
                            stringResource(Res.string.friends_screen_add_as_favorite_description),
                        tint = MaterialTheme.colorScheme.inversePrimary
                    )
                }
            },
            enabled = !isLoading
        )
    }
}
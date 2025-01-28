package com.makapp.kaizen.ui.screens.my_friends.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.makapp.kaizen.domain.models.FriendSearchPreview
import com.makapp.kaizen.ui.screens.components.PlaceholderText
import com.makapp.kaizen.ui.screens.my_friends.MyFriendsAction
import com.makapp.kaizen.ui.screens.my_friends.getTrailingIcon

@Composable
fun FriendSearchView(
    usernameInputValue: String,
    usernameInputError: String?,
    friendSearchPreview: FriendSearchPreview?,
    isSearchLoading: Boolean,
    isSendRequestLoading: Boolean,
    onAction: (MyFriendsAction) -> Unit
) {
    OutlinedTextField(
        value = usernameInputValue,
        onValueChange = { text ->
            onAction(MyFriendsAction.OnFriendUsernameInputChanged(text))
        },
        label = { Text("Add new friend") },
        placeholder = { PlaceholderText("Friend's username") },
        supportingText = if (friendSearchPreview == null && usernameInputError != null) {
            { Text(usernameInputError) }
        } else {
            null
        },
        textStyle = MaterialTheme.typography.bodyLarge,
        singleLine = true,
        isError = usernameInputError != null,
        shape = RoundedCornerShape(16.dp),
        trailingIcon = {
            getTrailingIcon(
                username = usernameInputValue,
                onAction = onAction,
                isLoading = isSearchLoading
            )
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = { onAction(MyFriendsAction.OnSearchFriendProfile) }
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .zIndex(2f)
    )

    val visible = friendSearchPreview != null
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            animationSpec = tween(300),
        ),
        modifier = Modifier.offset(y = (-16).dp)
    ) {
        friendSearchPreview?.let { friend ->
            FriendSearchPreviewView(
                friend = friend,
                isLoading = isSendRequestLoading,
                onAction = onAction
            )
        }
    }
}

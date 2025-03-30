package com.makapp.kaizen.ui.screens.account.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.makapp.kaizen.domain.models.user.User
import com.makapp.kaizen.ui.resources.avatars
import kaizen.composeapp.generated.resources.Res
import kaizen.composeapp.generated.resources.account_profile_picture_description
import kaizen.composeapp.generated.resources.account_profile_username
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun AccountHeaderView(
    user: User
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Image(
            painter = painterResource(avatars[user.profilePictureIndex].drawable),
            contentDescription = stringResource(Res.string.account_profile_picture_description),
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                user.getUsername(),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
            )
            Text(
                stringResource(Res.string.account_profile_username, user.name),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

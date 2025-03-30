package com.makapp.kaizen.ui.screens.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.makapp.kaizen.domain.models.friend.Friend
import com.makapp.kaizen.ui.resources.avatars
import com.makapp.kaizen.ui.screens.challenge_details.ChallengeDetailsNavArgs
import com.makapp.kaizen.ui.screens.home.HomeAction
import kaizen.composeapp.generated.resources.Res
import kaizen.composeapp.generated.resources.friend_empty_challenges_title
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun FriendWithChallengesView(
    friend: Friend,
    onAction: (HomeAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)) {
            Image(
                painter = painterResource(avatars[friend.profilePictureIndex].drawable),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
            )
            Text(
                friend.getUsername(),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
            )
        }
        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp).width(120.dp),
            color = MaterialTheme.colorScheme.onSurface
        )
        Column(modifier = Modifier.padding(top = 8.dp)) {
            if (friend.challenges.isEmpty()) {
                FriendViewEmptyChallengesView(friend.getUsername())
            }
            friend.challenges.forEach { challenge ->
                ChallengeView(
                    challenge,
                    onRowClick = {
                        onAction(
                            HomeAction.OnChallengeClicked(
                                navArgs = ChallengeDetailsNavArgs(
                                    challenge.id,
                                    readOnly = true
                                )
                            )
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun FriendViewEmptyChallengesView(
    friendName: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.surfaceContainer)
                .padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(Res.string.friend_empty_challenges_title, friendName),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
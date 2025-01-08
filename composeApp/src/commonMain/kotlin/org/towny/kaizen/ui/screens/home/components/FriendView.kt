package org.towny.kaizen.ui.screens.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.towny.kaizen.domain.models.User

@Composable
fun FriendView(
    user: User,
    onToggleChallenge: (challengeId: String, isChecked: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            user.name,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
        )
        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp).width(120.dp),
            color = MaterialTheme.colorScheme.onSurface
        )
        Column(modifier = Modifier.padding(top = 8.dp)) {
            user.challenges.forEach { challenge ->
                ChallengeView(
                    challenge = challenge,
                    onToggleChallenge = onToggleChallenge,
                )
            }
        }
    }
}
package org.towny.kaizen.ui.screens.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.towny.kaizen.domain.models.User
import org.towny.kaizen.ui.screens.home.HomeAction

@Composable
fun CurrentUserView(
    user: User,
    onAction: (HomeAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            "My challenges",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp),
        )
        HorizontalDivider(
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.width(120.dp).padding(top = 8.dp)
        )
        Column(modifier = Modifier.padding(top = 12.dp)) {
            user.challenges.forEach { challenge ->
                ChallengeView(
                    onToggleChallenge = { challengeId: String, isChecked: Boolean ->
                        onAction(HomeAction.OnToggleChallenge(user.id, challengeId, isChecked))
                    },
                    challenge = challenge,
                    belongToCurrentUser = true
                )
            }
        }
    }
}
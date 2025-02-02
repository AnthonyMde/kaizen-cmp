package com.makapp.kaizen.ui.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.makapp.kaizen.domain.models.User
import com.makapp.kaizen.ui.screens.challenge_details.ChallengeDetailsNavArgs
import com.makapp.kaizen.ui.screens.components.FormErrorText
import com.makapp.kaizen.ui.screens.home.HomeAction

@Composable
fun CurrentUserView(
    user: User?,
    onAction: (HomeAction) -> Unit,
    modifier: Modifier = Modifier,
    error: String? = null,
    isLoading: Boolean,
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        if (error != null) {
            FormErrorText(error, textAlign = TextAlign.Center)
            return@Column
        }

        Text(
            "My challenges",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp),
        )
        HorizontalDivider(
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.width(120.dp).padding(top = 8.dp)
        )

        if (user == null || isLoading) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .padding(top = 8.dp)
                    .padding(horizontal = 24.dp)
            )
        }

        Column(modifier = Modifier.padding(top = 12.dp)) {
            if (user != null && user.challenges.isEmpty() && !isLoading) {
                CurrentUserEmptyChallengesView(
                    onAction = onAction
                )
            } else {
                user?.challenges?.forEach { challenge ->
                    ChallengeView(
                        onToggleChallenge = { challengeId: String, isChecked: Boolean ->
                            onAction(HomeAction.OnToggleChallenge(user.id, challengeId, isChecked))
                        },
                        challenge = challenge,
                        belongToCurrentUser = true,
                        onClick = {
                            onAction(
                                HomeAction.OnChallengeClicked(
                                    navArgs = ChallengeDetailsNavArgs(
                                        challenge.id,
                                        challenge.name,
                                        challenge.isDoneForToday
                                    )
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CurrentUserEmptyChallengesView(
    modifier: Modifier = Modifier,
    onAction: (action: HomeAction) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable {
                onAction(HomeAction.OnCreateFirstChallengeClicked)
            },
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
                    text = "No Challenges Yet",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Tap to create a new challenge!",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}
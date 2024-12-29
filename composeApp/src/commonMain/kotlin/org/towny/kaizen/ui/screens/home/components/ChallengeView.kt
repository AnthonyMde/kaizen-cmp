package org.towny.kaizen.ui.screens.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import kaizen.composeapp.generated.resources.Res
import kaizen.composeapp.generated.resources.challenge_failed_stamp
import org.jetbrains.compose.resources.painterResource
import org.towny.kaizen.domain.models.Challenge
import org.towny.kaizen.ui.theme.customColors

@Composable
fun ChallengeView(
    challenge: Challenge,
    onToggleChallenge: (challengeId: String, isChecked: Boolean) -> Unit,
    belongToCurrentUser: Boolean = false
) {
    Box(
        Modifier.padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = if (challenge.isCompleted) MaterialTheme.colorScheme.tertiaryContainer
                    else MaterialTheme.colorScheme.secondaryContainer,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            Checkbox(
                checked = challenge.isCompleted,
                onCheckedChange = { isChecked ->
                    onToggleChallenge(challenge.id, isChecked)
                },
                enabled = belongToCurrentUser && !challenge.isFailed(),
                colors = CheckboxDefaults.colors()
                    .copy(
                        checkedBoxColor = MaterialTheme.colorScheme.tertiary,
                        checkedBorderColor = MaterialTheme.colorScheme.tertiary,
                        disabledUncheckedBorderColor = if (challenge.isFailed())
                            MaterialTheme.customColors.failedChallengeText
                        else CheckboxDefaults.colors().disabledBorderColor
                    )
            )
            Text(
                challenge.name,
                color = when {
                    challenge.isFailed() -> MaterialTheme.customColors.failedChallengeText
                    challenge.isCompleted -> MaterialTheme.colorScheme.onTertiaryContainer
                    else -> MaterialTheme.colorScheme.onSecondaryContainer
                },
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
        if (challenge.isFailed()) {
            Box(
                Modifier.matchParentSize()
                    .background(
                        color = Color.Black.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                Image(
                    painter = painterResource(Res.drawable.challenge_failed_stamp),
                    contentDescription = null,
                    modifier = Modifier
                        .height(35.dp)
                        .width(100.dp)
                        .padding(end = 24.dp)
                        .align(Alignment.CenterEnd),
                    contentScale = ContentScale.FillBounds
                )
            }
        }
    }
}
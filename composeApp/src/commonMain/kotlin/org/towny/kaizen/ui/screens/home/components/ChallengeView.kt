package org.towny.kaizen.ui.screens.home.components

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kaizen.composeapp.generated.resources.Res
import kaizen.composeapp.generated.resources.challenge_failed_stamp
import org.jetbrains.compose.resources.painterResource
import org.towny.kaizen.domain.models.Challenge
import org.towny.kaizen.ui.theme.customColors

@Composable
fun ChallengeView(
    challenge: Challenge,
    onToggleChallenge: ((challengeId: String, isChecked: Boolean) -> Unit)? = null,
    belongToCurrentUser: Boolean = false
) {
    Box(
        Modifier.padding(vertical = 4.dp)
    ) {
        val backgroundColor by animateColorAsState(
            targetValue = if (challenge.isDoneForToday) MaterialTheme.colorScheme.tertiaryContainer
            else MaterialTheme.colorScheme.secondaryContainer
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = challenge.isDoneForToday,
                onCheckedChange = { isChecked ->
                    onToggleChallenge?.invoke(challenge.id, isChecked)
                },
                enabled = belongToCurrentUser && !challenge.isFailed(),
                colors = CheckboxDefaults.colors()
                    .copy(
                        checkedBoxColor = MaterialTheme.colorScheme.tertiary,
                        checkedBorderColor = MaterialTheme.colorScheme.tertiary,
                        disabledUncheckedBorderColor = if (challenge.isFailed())
                            MaterialTheme.customColors.failedChallengeText
                        else CheckboxDefaults.colors().disabledBorderColor
                    ),
                modifier = Modifier.padding(start = 8.dp)
            )
            Text(
                challenge.name,
                style = MaterialTheme.typography.bodyLarge,
                color = challenge.getChallengeTextColor(),
                modifier = Modifier.weight(1f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                "Day ${challenge.days}",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontSize = 16.sp
                ),
                color = when {
                    challenge.isFailed() -> MaterialTheme.customColors.failedChallengeText
                    challenge.isDoneForToday -> MaterialTheme.colorScheme.tertiary
                    else -> MaterialTheme.colorScheme.secondary
                },
                modifier = Modifier.padding(end = 16.dp),
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

@Composable
private fun Challenge.getChallengeTextColor(): Color {
    return when {
        isFailed() -> MaterialTheme.customColors.failedChallengeText
        isDoneForToday -> MaterialTheme.colorScheme.onTertiaryContainer
        else -> MaterialTheme.colorScheme.onSecondaryContainer
    }
}
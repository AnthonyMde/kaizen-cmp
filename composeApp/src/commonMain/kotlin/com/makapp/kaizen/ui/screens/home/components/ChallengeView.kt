package com.makapp.kaizen.ui.screens.home.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.makapp.kaizen.domain.models.Challenge
import com.makapp.kaizen.ui.theme.customColors
import kaizen.composeapp.generated.resources.Res
import kaizen.composeapp.generated.resources.challenge_day_count
import kaizen.composeapp.generated.resources.challenge_failed_stamp
import kaizen.composeapp.generated.resources.challenge_row_on_paused_text
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ChallengeView(
    challenge: Challenge,
    onToggleChallenge: ((challengeId: String, isChecked: Boolean) -> Unit)? = null,
    belongToCurrentUser: Boolean = false,
    onRowClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(vertical = 4.dp)
    ) {
        val backgroundColor by animateColorAsState(
            targetValue = if (challenge.isDoneForToday) MaterialTheme.colorScheme.tertiaryContainer
            else MaterialTheme.colorScheme.secondaryContainer
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .clickable {
                    onRowClick()
                }
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
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )
            Text(
                stringResource(Res.string.challenge_day_count, challenge.days),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold
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
        } else if (challenge.isPaused()) {
            Box(
                Modifier.matchParentSize()
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f),
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    stringResource(Res.string.challenge_row_on_paused_text)
                        .toUpperCase(Locale.current),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = TextUnit(0.8f, TextUnitType.Sp),
                    ),
                    color = MaterialTheme.customColors.challengeCardContainerDone.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
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
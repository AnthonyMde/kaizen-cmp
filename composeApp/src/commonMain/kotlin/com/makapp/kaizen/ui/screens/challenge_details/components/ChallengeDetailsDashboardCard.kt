package com.makapp.kaizen.ui.screens.challenge_details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.makapp.kaizen.domain.models.Challenge
import com.makapp.kaizen.ui.screens.challenge_details.ChallengeDetailsAction
import com.makapp.kaizen.ui.screens.challenge_details.ChallengeDetailsViewModel
import com.makapp.kaizen.utils.DateUtils.toShortDateFormat
import kaizen.composeapp.generated.resources.Res
import kaizen.composeapp.generated.resources.challenge_details_dashboard_life_counter_description
import kaizen.composeapp.generated.resources.challenge_status_abandoned
import kaizen.composeapp.generated.resources.challenge_status_done
import kaizen.composeapp.generated.resources.challenge_status_failed
import kaizen.composeapp.generated.resources.challenge_status_ongoing
import kaizen.composeapp.generated.resources.challenge_status_paused
import kaizen.composeapp.generated.resources.ic_outline_cake
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun ChallengeDetailsDashboardCard(
    challenge: Challenge,
    isEditable: Boolean,
    onAction: (ChallengeDetailsAction) -> Unit
) {
    val viewModel = koinInject<ChallengeDetailsViewModel>()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_outline_cake),
                    contentDescription = null,
                )
                Text(
                    challenge.createdAt.toShortDateFormat(),
                    modifier = Modifier.align(Alignment.Bottom),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(
                    modifier = Modifier.size(28.dp),
                    onClick = {
                        onAction(
                            ChallengeDetailsAction.GoToChallengeInfos(
                                challenge.name,
                                challenge.maxAuthorizedFailures
                            )
                        )
                    },
                    enabled = viewModel.isHeartButtonClickable(isEditable, challenge)) {
                    Icon(
                        painter = painterResource(
                            viewModel.getHeartIcon(
                                maxFailures = challenge.maxAuthorizedFailures,
                                failures = challenge.failureCount
                            )
                        ),
                        tint = MaterialTheme.colorScheme.error,
                        contentDescription = stringResource(Res.string.challenge_details_dashboard_life_counter_description),
                        modifier = Modifier.fillMaxSize()
                    )
                }
                if (!challenge.isFailed()) {
                    Text(
                        "${challenge.maxAuthorizedFailures - challenge.failureCount}/${challenge.maxAuthorizedFailures}",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
                    )
                }
            }
        }

        ChallengeDaysCircularProgress(challenge.days)

        Button(
            onClick = {
                onAction(ChallengeDetailsAction.OnStatusButtonClicked)
            },
            enabled = isEditable,
            colors = if (challenge.isPaused() || challenge.isAbandoned()) {
                ButtonDefaults.buttonColors().copy(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                ButtonDefaults.buttonColors().copy(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onTertiary
                )
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Row {
                if (isEditable) Spacer(modifier = Modifier.width(8.dp))
                Text(text = getChallengeStatusText(challenge.status))
                if (isEditable) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null,
                    )
                }
            }
        }
    }
}

@Composable
private fun getChallengeStatusText(status: Challenge.Status): String = when (status) {
    Challenge.Status.ON_GOING -> {
        stringResource(Res.string.challenge_status_ongoing)
    }

    Challenge.Status.PAUSED -> {
        stringResource(Res.string.challenge_status_paused)
    }
    Challenge.Status.DONE -> {
        stringResource(Res.string.challenge_status_done)
    }
    Challenge.Status.FAILED -> {
        stringResource(Res.string.challenge_status_failed)
    }

    Challenge.Status.ABANDONED -> {
        stringResource(Res.string.challenge_status_abandoned)
    }
}

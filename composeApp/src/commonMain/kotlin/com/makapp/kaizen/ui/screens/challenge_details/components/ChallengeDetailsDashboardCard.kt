package com.makapp.kaizen.ui.screens.challenge_details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.makapp.kaizen.domain.models.Challenge
import com.makapp.kaizen.ui.screens.challenge_details.ChallengeDetailsViewModel
import com.makapp.kaizen.utils.DateUtils.toShortDateFormat
import kaizen.composeapp.generated.resources.Res
import kaizen.composeapp.generated.resources.ic_outline_cake
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject

@Composable
fun ChallengeDetailsDashboardCard(
    challenge: Challenge,
    readOnly: Boolean
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
                Icon(
                    painter = painterResource(
                        viewModel.getHeartIcon(
                            maxFailures = challenge.maxAuthorizedFailures,
                            failures = challenge.failureCount
                        )
                    ),
                    tint = MaterialTheme.colorScheme.error,
                    contentDescription = "Remaining life counter.",
                    modifier = Modifier
                        .size(28.dp)
                )
                Text(
                    "${challenge.maxAuthorizedFailures - challenge.failureCount}/${challenge.maxAuthorizedFailures}",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
                )
            }
        }

        ChallengeDaysCircularProgress(challenge.days)

        Button(
            onClick = {},
            enabled = !readOnly,
            colors = ButtonDefaults.buttonColors().copy(
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onTertiary
            ),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Row {
                if (!readOnly) Spacer(modifier = Modifier.width(8.dp))
                Text(text = viewModel.getChallengeStatusText(challenge.status))
                if (!readOnly) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Change challenge's status.",
                    )
                }
            }
        }
    }
}

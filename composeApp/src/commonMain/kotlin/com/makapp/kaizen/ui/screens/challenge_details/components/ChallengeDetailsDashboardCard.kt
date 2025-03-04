package com.makapp.kaizen.ui.screens.challenge_details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
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
import com.makapp.kaizen.utils.DateUtils.toShortDateFormat
import kaizen.composeapp.generated.resources.Res
import kaizen.composeapp.generated.resources.ic_heart
import kaizen.composeapp.generated.resources.ic_outline_cake
import org.jetbrains.compose.resources.painterResource

@Composable
fun ChallengeDetailsDashboardCard(
    challenge: Challenge,
    getChallengeStatusName: (Challenge.Status) -> String
) {
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
                .padding(horizontal = 16.dp)
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
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Icon(
                painter = painterResource(Res.drawable.ic_heart),
                contentDescription = "Remaining life counter."
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 16.dp),
            contentAlignment = Alignment.CenterEnd,
        ) {
            Text(
                "7/12",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
            )
        }

        // TODO: counter here

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors().copy(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
            ),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Row {
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = getChallengeStatusName(challenge.status))
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Change challenge's status.",
                )
            }
        }
    }
}

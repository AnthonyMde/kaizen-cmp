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
import org.towny.kaizen.domain.entities.Challenger

@Composable
fun ChallengerView(
    challenger: Challenger
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(challenger.name, color = MaterialTheme.colorScheme.onBackground, style = MaterialTheme.typography.titleMedium)
        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp).width(120.dp),
            color = MaterialTheme.colorScheme.onBackground
        )
        Column(modifier = Modifier.padding(top = 8.dp)) {
            challenger.challenges.forEach { challenge ->
                ChallengeView(challenge)
            }
        }
    }
}
package org.towny.kaizen.ui.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.towny.kaizen.domain.models.Challenge

@Composable
fun ChallengeView(
    challenge: Challenge,
    onToggleChallenge: (challengeId: String, isChecked: Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(
                color = if (challenge.isCompleted)
                    MaterialTheme.colorScheme.tertiaryContainer
                else
                    MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(16.dp)
            ).padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Checkbox(
            checked = challenge.isCompleted,
            onCheckedChange = { isChecked ->
                onToggleChallenge(challenge.id, isChecked)
            },
            colors = CheckboxDefaults.colors()
                .copy(
                    checkedBoxColor = MaterialTheme.colorScheme.tertiary,
                    checkedBorderColor = MaterialTheme.colorScheme.tertiary
                )
        )
        Text(
            challenge.name,
            color = if (challenge.isCompleted)
                MaterialTheme.colorScheme.onTertiaryContainer
            else
                MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}
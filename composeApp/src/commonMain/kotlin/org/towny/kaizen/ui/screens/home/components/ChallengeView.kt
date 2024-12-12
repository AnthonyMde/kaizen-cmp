package org.towny.kaizen.ui.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.towny.kaizen.domain.entities.Challenge

@Composable
fun ChallengeView(
    challenge: Challenge
) {
    val backgroundColor = if (challenge.isCompleted) {
        Color.Green
    } else {
        Color.Red
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(16.dp)
            ).padding(all = 12.dp)
    ) {
        Checkbox(checked = challenge.isCompleted, onCheckedChange = {})
        Text(
            challenge.name,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}
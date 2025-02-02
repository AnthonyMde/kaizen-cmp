package com.makapp.kaizen.ui.screens.challenge_details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ChallengeDetailsSecondRowView(
    failureCount: Int,
    maxAuthorizedFailures: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .height(120.dp)
                .weight(1f)
                .clip(RoundedCornerShape(16.dp))
                .background(color = Color(0xFFEDEBE7))
                .clickable {

                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "My diary",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontFamily = FontFamily.Cursive,
                ),
                textAlign = TextAlign.Center,
                color = Color.Black
            )
        }

        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(color = Color(0xFFD48A73))
                .clickable {

                },
        ) {
            Column(
                modifier = Modifier.matchParentSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "${failureCount}/${maxAuthorizedFailures}",
                    style = MaterialTheme.typography.displaySmall,
                    color = Color(0xFF774C3F),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                )
                Text(
                    text = "errors",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF774C3F),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

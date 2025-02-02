package com.makapp.kaizen.ui.screens.challenge_details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.makapp.kaizen.ui.screens.challenge_details.ChallengeDetailsAction
import com.makapp.kaizen.ui.screens.challenge_details.ChallengeDetailsNavArgs
import com.makapp.kaizen.utils.StringUtils

@Composable
fun ChallengeDetailsHeaderView(
    navArgs: ChallengeDetailsNavArgs,
    onAction: (ChallengeDetailsAction) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(
                    topEnd = 0.dp,
                    topStart = 0.dp,
                    bottomStart = 16.dp,
                    bottomEnd = 16.dp
                )
            )
            .background(
                if (navArgs.isDone)
                    MaterialTheme.colorScheme.tertiaryContainer
                else
                    MaterialTheme.colorScheme.secondaryContainer
            )
            .systemBarsPadding(),
    ) {
        IconButton(
            onClick = { onAction(ChallengeDetailsAction.OnNavigateUp) },
            content = {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = "Go back"
                )
            },
            modifier = Modifier.align(Alignment.TopStart)
        )
        Text(
            text = StringUtils.uppercaseEachWord(navArgs.title),
            style = MaterialTheme.typography.headlineMedium
                .copy(
                    letterSpacing = TextUnit(1f, TextUnitType.Sp),
                    fontWeight = FontWeight.Medium
                ),
            color = Color(0xFF655046),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 36.dp)
                .padding(bottom = 24.dp, top = 48.dp)
                .align(Alignment.BottomCenter)
        )
    }
}

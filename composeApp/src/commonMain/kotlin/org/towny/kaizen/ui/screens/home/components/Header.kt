package org.towny.kaizen.ui.screens.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kaizen.composeapp.generated.resources.Res
import kaizen.composeapp.generated.resources.avatar_1_x3
import org.jetbrains.compose.resources.painterResource
import org.towny.kaizen.ui.screens.home.HomeAction
import org.towny.kaizen.utils.DateUtils

@Composable
fun Header(
    onAction: (HomeAction) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = DateUtils.getTodaysDate(),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(2f)
        )

        Image(
            painter = painterResource(Res.drawable.avatar_1_x3),
            contentDescription = "Go to your profile.",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .size(55.dp)
                .clip(CircleShape)
                .clickable {
                    onAction(HomeAction.OnAccountClicked)
                }
        )
    }
}

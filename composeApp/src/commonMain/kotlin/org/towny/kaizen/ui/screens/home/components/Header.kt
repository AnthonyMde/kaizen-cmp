package org.towny.kaizen.ui.screens.home.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.towny.kaizen.ui.screens.home.HomeAction
import org.towny.kaizen.utils.DateUtils

@Composable
fun Header(
    isLogoutLoading: Boolean,
    onAction: (HomeAction) -> Unit
) {
    Box {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = DateUtils.getTodaysDate(),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
            )
            Text(
                text = "Day ${DateUtils.getNumberOfDaysSince()}",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.primary
            )
        }
        IconButton(
            onClick = {
                onAction(HomeAction.OnLogout)
            },
            enabled = !isLogoutLoading,
            content = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = "Logout",
                    modifier = Modifier.size(32.dp)
                )
            },
            colors = IconButtonDefaults.iconButtonColors()
                .copy(contentColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.5f)),
            modifier = Modifier
                .align(Alignment.CenterEnd)
        )
    }
}


package com.makapp.kaizen.ui.screens.challenge_details.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.makapp.kaizen.ui.screens.challenge_details.ChallengeDetailsAction
import kaizen.composeapp.generated.resources.Res
import kaizen.composeapp.generated.resources.ic_heart
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun ChallengeDetailsDropDownMenu(
    onAction: (ChallengeDetailsAction) -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    IconButton(
        onClick = {
            expanded = true
        },
        content = {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Challenge settings",
                tint = MaterialTheme.colorScheme.secondary
            )
        },
    )

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
    ) {
        items.forEach { item ->
            DropdownMenuItem(
                text = {
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(
                            painter = painterResource(item.icon),
                            contentDescription = null
                        )
                        Text(item.title)
                    }
                },
                onClick = {
                    expanded = false
                    onAction(item.action)
                }
            )
        }
    }
}

data class ChallengeDetailsDropDownMenuItem(
    val title: String,
    val icon: DrawableResource,
    val action: ChallengeDetailsAction
)

private val items = listOf(
    ChallengeDetailsDropDownMenuItem(
        title = "Rename",
        icon = Res.drawable.ic_heart,
        action = ChallengeDetailsAction.GoToChallengeInfos
    ),
    ChallengeDetailsDropDownMenuItem(
        title = "Abandon",
        icon = Res.drawable.ic_heart,
        action = ChallengeDetailsAction.OnAbandonChallengeClicked
    ),
    ChallengeDetailsDropDownMenuItem(
        title = "Delete",
        icon = Res.drawable.ic_heart,
        action = ChallengeDetailsAction.OnDeleteChallengeClicked
    ),
)

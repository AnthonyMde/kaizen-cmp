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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.makapp.kaizen.domain.models.challenge.Challenge
import com.makapp.kaizen.ui.screens.challenge_details.ChallengeDetailsAction
import kaizen.composeapp.generated.resources.Res
import kaizen.composeapp.generated.resources.challenge_details_drop_down_menu_delete
import kaizen.composeapp.generated.resources.challenge_details_drop_down_menu_description
import kaizen.composeapp.generated.resources.challenge_details_drop_down_menu_rename
import kaizen.composeapp.generated.resources.ic_bin_outlined
import kaizen.composeapp.generated.resources.ic_pen_outlined
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ChallengeDetailsDropDownMenu(
    challenge: Challenge,
    onAction: (ChallengeDetailsAction) -> Unit,
) {
    val items = listOf(
        ChallengeDetailsDropDownMenuItem(
            title = stringResource(Res.string.challenge_details_drop_down_menu_rename),
            icon = Res.drawable.ic_pen_outlined,
            action = ChallengeDetailsAction.GoToChallengeInfos(
                challenge.name,
                challenge.maxAuthorizedFailures
            ),
            color = MaterialTheme.colorScheme.onSurface
        ),
        ChallengeDetailsDropDownMenuItem(
            title = stringResource(Res.string.challenge_details_drop_down_menu_delete),
            icon = Res.drawable.ic_bin_outlined,
            action = ChallengeDetailsAction.OnDeleteChallengeClicked,
            color = MaterialTheme.colorScheme.error
        ),
    )

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
                contentDescription = stringResource(Res.string.challenge_details_drop_down_menu_description),
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
                            contentDescription = null,
                            tint = item.color
                        )
                        Text(item.title, color = item.color)
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
    val action: ChallengeDetailsAction,
    val color: Color
)

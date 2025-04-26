package com.makapp.kaizen.ui.screens.onboarding.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.makapp.kaizen.ui.models.Avatar
import com.makapp.kaizen.ui.screens.onboarding.OnBoardingProfileAction
import kaizen.composeapp.generated.resources.Res
import kaizen.composeapp.generated.resources.onboarding_profile_screen_avatar_section_subtitle
import kaizen.composeapp.generated.resources.onboarding_profile_screen_avatar_section_title
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalLayoutApi::class)
@Suppress("FunctionName")
fun LazyListScope.AvatarGrid(
    avatars: List<Avatar>,
    avatarSelectedIndex: Int,
    onAction: (OnBoardingProfileAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    item {
        Text(
            stringResource(Res.string.onboarding_profile_screen_avatar_section_title),
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            stringResource(
                Res.string.onboarding_profile_screen_avatar_section_subtitle,
                "pikisuperstar"
            ),
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
    }

    item {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            avatars.forEachIndexed { index, avatar ->
                val isSelected = avatarSelectedIndex == index
                Box {
                    Image(painter = painterResource(avatar.drawable),
                        contentDescription = stringResource(avatar.description),
                        modifier = Modifier.clip(CircleShape).clickable {
                            onAction(OnBoardingProfileAction.OnAvatarSelected(index))
                        })
                    if (!isSelected) {
                        Box(
                            modifier = Modifier.matchParentSize().clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.5f))
                        )
                    }
                }
            }
        }
    }
}
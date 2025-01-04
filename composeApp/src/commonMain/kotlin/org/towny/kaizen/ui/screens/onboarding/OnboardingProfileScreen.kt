package org.towny.kaizen.ui.screens.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kaizen.composeapp.generated.resources.Res
import kaizen.composeapp.generated.resources.avatar_1_x3
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun OnboardingProfileScreen(
    selectedAvatarIndex: Int = 0,
    onAvatarSelected: (Int) -> Unit
) {
    val avatars = remember {
        listOf(
            Avatar(
                drawable = Res.drawable.avatar_1_x3,
                description = "Avatar of a brown guy"
            ),
            Avatar(
                drawable = Res.drawable.avatar_1_x3,
                description = "Avatar of a brown guy"
            ),
            Avatar(
                drawable = Res.drawable.avatar_1_x3,
                description = "Avatar of a brown guy"
            ),
            Avatar(
                drawable = Res.drawable.avatar_1_x3,
                description = "Avatar of a brown guy"
            ),
            Avatar(
                drawable = Res.drawable.avatar_1_x3,
                description = "Avatar of a brown guy"
            ),
            Avatar(
                drawable = Res.drawable.avatar_1_x3,
                description = "Avatar of a brown guy"
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(24.dp)
    ) {
        Image(
            painter = painterResource(avatars[selectedAvatarIndex].drawable),
            contentDescription = avatars[selectedAvatarIndex].description,
            modifier = Modifier
                .clip(CircleShape)
                .align(Alignment.CenterHorizontally)
                .size(80.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("What's your name?",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = "",
            onValueChange = {},
            label = {
                Text("username",
                    color = Color.LightGray)
            },
            singleLine = true,
            supportingText = {
                Text("It will be visible by others.")
            },
            colors = TextFieldDefaults.colors().copy(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        HorizontalDivider(
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .height(2.dp)
                .padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text("avatars",
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Adaptive(50.dp),
            content = {
                itemsIndexed(avatars) { index, avatar ->
                    Box(
                        modifier = Modifier
                            .border(
                                2.dp,
                                if (avatars.indexOf(avatar) == index) Color.Blue else Color.Transparent,
                                CircleShape
                            )
                    ) {
                        Image(
                            painter = painterResource(avatar.drawable),
                            contentDescription = avatar.description,
                            modifier = Modifier
                                .clip(CircleShape)
                        )
                    }
                }
            },
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
        )
    }
}

data class Avatar(
    val drawable: DrawableResource,
    val description: String
)

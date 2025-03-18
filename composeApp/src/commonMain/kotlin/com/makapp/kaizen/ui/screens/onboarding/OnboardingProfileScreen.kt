package com.makapp.kaizen.ui.screens.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.makapp.kaizen.ui.models.Avatar
import com.makapp.kaizen.ui.resources.avatars
import com.makapp.kaizen.ui.screens.components.LoadingButton
import kaizen.composeapp.generated.resources.Res
import kaizen.composeapp.generated.resources.onboarding_profile_screen_avatar_section_subtitle
import kaizen.composeapp.generated.resources.onboarding_profile_screen_avatar_section_title
import kaizen.composeapp.generated.resources.onboarding_profile_screen_displayname_input_label
import kaizen.composeapp.generated.resources.onboarding_profile_screen_displayname_input_support_text
import kaizen.composeapp.generated.resources.onboarding_profile_screen_submit_button
import kaizen.composeapp.generated.resources.onboarding_profile_screen_title
import kaizen.composeapp.generated.resources.onboarding_profile_screen_username_input_label
import kaizen.composeapp.generated.resources.onboarding_profile_screen_username_support_text
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun OnboardingProfileScreenRoot(
    viewModel: OnboardingProfileViewModel = koinInject(), goToHomeScreen: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(true) {
        viewModel.navigationEvents.collectLatest { event ->
            when (event) {
                OnBoardingProfileNavigationEvent.GoToHomeScreen -> {
                    goToHomeScreen()
                }
            }
        }
    }

    OnboardingProfileScreen(state = state, onAction = { action ->
        viewModel.onAction(action)
    })
}

@Composable
fun OnboardingProfileScreen(
    state: OnBoardingProfileScreenState, onAction: (OnBoardingProfileAction) -> Unit
) {
    val keyboard = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val avatars: List<Avatar> = remember { avatars }

    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface)
            .systemBarsPadding().pointerInput(null) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }.padding(horizontal = 24.dp).imePadding()
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Image(
            painter = painterResource(avatars[state.avatarSelectedIndex].drawable),
            contentDescription = stringResource(avatars[state.avatarSelectedIndex].description),
            modifier = Modifier.clip(CircleShape).align(Alignment.CenterHorizontally).size(90.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            stringResource(Res.string.onboarding_profile_screen_title),
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = state.usernameInputValue,
            onValueChange = { username ->
                onAction(OnBoardingProfileAction.OnUsernameInputValueChanged(username))
            },
            label = { Text(stringResource(Res.string.onboarding_profile_screen_username_input_label)) },
            singleLine = true,
            supportingText = {
                val stringRes = state.usernameInputError
                    ?: Res.string.onboarding_profile_screen_username_support_text
                Text(stringResource(stringRes))
            },
            isError = state.usernameInputError != null,
            trailingIcon = {
                if (state.usernameInputError != null) Icon(
                    Icons.Default.Warning, contentDescription = null
                )
            },
            colors = TextFieldDefaults.colors().copy(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(8.dp),
            keyboardOptions = KeyboardOptions().copy(
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = state.displayNameInputValue,
            onValueChange = { displayName ->
                onAction(OnBoardingProfileAction.OnDisplayNameInputValueChanged(displayName))
            },
            label = { Text(stringResource(Res.string.onboarding_profile_screen_displayname_input_label)) },
            singleLine = true,
            supportingText = {
                val stringRes = state.displayNameInputError
                    ?: Res.string.onboarding_profile_screen_displayname_input_support_text
                Text(stringResource(stringRes))
            },
            isError = state.displayNameInputError != null,
            trailingIcon = {
                if (state.displayNameInputError != null) Icon(
                    Icons.Default.Warning, contentDescription = null
                )
            },
            colors = TextFieldDefaults.colors().copy(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(8.dp),
            keyboardOptions = KeyboardOptions().copy(
                imeAction = ImeAction.Done,
                capitalization = KeyboardCapitalization.Sentences
            ),
            keyboardActions = KeyboardActions(onDone = {
                keyboard?.hide()
            }),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        HorizontalDivider(
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.height(2.dp).padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(2.dp))

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

        LazyVerticalGrid(
            columns = GridCells.Adaptive(50.dp),
            content = {
                itemsIndexed(avatars) { index, avatar ->
                    val isSelected = state.avatarSelectedIndex == index
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
            },
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
        )

        Spacer(modifier = Modifier.weight(1f))

        LoadingButton(
            onClick = {
                submit(keyboard, onAction)
            },
            enabled = !state.isFormSubmissionLoading,
            isLoading = state.isFormSubmissionLoading,
            label = stringResource(Res.string.onboarding_profile_screen_submit_button),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

private fun submit(
    keyboard: SoftwareKeyboardController?, onAction: (OnBoardingProfileAction) -> Unit
) {
    keyboard?.hide()
    onAction(OnBoardingProfileAction.OnSubmitProfile)
}

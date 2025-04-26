package com.makapp.kaizen.ui.screens.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.makapp.kaizen.ui.models.Avatar
import com.makapp.kaizen.ui.resources.avatars
import com.makapp.kaizen.ui.screens.onboarding.component.AvatarGrid
import com.makapp.kaizen.ui.screens.onboarding.component.CreateAccountButton
import com.makapp.kaizen.ui.screens.onboarding.component.DisplayNameInputField
import com.makapp.kaizen.ui.screens.onboarding.component.UsernameInputField
import kaizen.composeapp.generated.resources.Res
import kaizen.composeapp.generated.resources.onboarding_profile_screen_title
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .systemBarsPadding()
            .pointerInput(null) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }
            .imePadding()
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(bottom = 96.dp, start = 24.dp, end = 24.dp),
            modifier = Modifier
                .align(Alignment.TopCenter)
        ) {
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Image(
                    painter = painterResource(avatars[state.avatarSelectedIndex].drawable),
                    contentDescription = stringResource(avatars[state.avatarSelectedIndex].description),
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(90.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    stringResource(Res.string.onboarding_profile_screen_title),
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                UsernameInputField(
                    value = state.usernameInputValue,
                    error = state.usernameInputError,
                    onAction,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                DisplayNameInputField(
                    value = state.displayNameInputValue,
                    error = state.displayNameInputError,
                    onAction,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(32.dp))

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.height(2.dp).padding(horizontal = 24.dp)
                )

                Spacer(modifier = Modifier.height(2.dp))
            }

            AvatarGrid(
                avatars = avatars,
                avatarSelectedIndex = state.avatarSelectedIndex,
                onAction = onAction,
            )
        }

        CreateAccountButton(
            isFormSubmissionLoading = state.isFormSubmissionLoading,
            onClick = {
                submit(keyboard, onAction)
            }
        )
    }
}

private fun submit(
    keyboard: SoftwareKeyboardController?, onAction: (OnBoardingProfileAction) -> Unit
) {
    keyboard?.hide()
    onAction(OnBoardingProfileAction.OnSubmitProfile)
}

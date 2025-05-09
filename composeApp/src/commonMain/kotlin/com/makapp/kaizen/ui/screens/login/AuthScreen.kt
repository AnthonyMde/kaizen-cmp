package com.makapp.kaizen.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.makapp.kaizen.ui.components.FormErrorText
import com.makapp.kaizen.ui.components.LoadingButton
import com.makapp.kaizen.ui.components.PasswordTextField
import com.makapp.kaizen.ui.components.PlaceholderText
import com.makapp.kaizen.ui.screens.login.component.ResetPasswordModalView
import com.makapp.kaizen.ui.screens.login.component.ResetPasswordSentModal
import kaizen.composeapp.generated.resources.Res
import kaizen.composeapp.generated.resources.auth_screen_email_input_label
import kaizen.composeapp.generated.resources.auth_screen_email_input_placeholder
import kaizen.composeapp.generated.resources.auth_screen_forgot_password
import kaizen.composeapp.generated.resources.auth_screen_password_input_label
import kaizen.composeapp.generated.resources.auth_screen_password_input_placeholder
import kaizen.composeapp.generated.resources.auth_screen_submit_button
import kaizen.composeapp.generated.resources.auth_screen_welcoming_text
import kaizen.composeapp.generated.resources.landscape_icon
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AuthScreenRoot(
    viewModel: AuthViewModel = koinViewModel(),
    goToHomeScreen: () -> Unit,
    goToOnboardingProfile: () -> Unit
) {
    val loginScreenState by viewModel.state.collectAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(true) {
        scope.launch {
            viewModel.events.collectLatest { event ->
                when (event) {
                    AuthEvent.GoToHomeScreen -> goToHomeScreen()
                    AuthEvent.GoToOnboardingProfile -> goToOnboardingProfile()
                }
            }
        }
    }

    AuthScreen(
        state = loginScreenState,
        onAction = viewModel::onAction,
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.surface)
            .systemBarsPadding()
    )
}

@Composable
fun AuthScreen(
    state: AuthScreenState,
    onAction: (AuthAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val keyboard = LocalSoftwareKeyboardController.current
    val focus = LocalFocusManager.current
    val scroll = rememberScrollState()

    if (state.isResetPasswordModalDisplayed) {
        ResetPasswordModalView(
            emailValue = state.emailToSendForgotPasswordResetLink,
            isLoading = state.isResetPasswordRequestLoading,
            error = state.resetPasswordError,
            onAction = onAction
        )
    }

    if (state.isResetPasswordSentModalDisplayed) {
        ResetPasswordSentModal(
            email = state.emailToSendForgotPasswordResetLink,
            onAction = onAction
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(null) {
                detectTapGestures(
                    onTap = { focus.clearFocus() }
                )
            }
            .imePadding()
    ) {
        Column(
            modifier = modifier
                .fillMaxHeight()
                .verticalScroll(scroll)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(Res.drawable.landscape_icon),
                contentDescription = null,
                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.primary),
                modifier = Modifier.size(64.dp)
            )
            Text(
                text = stringResource(Res.string.auth_screen_welcoming_text),
                style = MaterialTheme.typography.headlineMedium,
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = state.emailInputValue,
                onValueChange = { text ->
                    onAction(AuthAction.OnEmailInputTextChanged(text))
                },
                label = { Text(stringResource(Res.string.auth_screen_email_input_label)) },
                placeholder = { PlaceholderText(stringResource(Res.string.auth_screen_email_input_placeholder)) },
                singleLine = true,
                isError = state.emailInputError != null,
                keyboardOptions = KeyboardOptions().copy(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
            )

            state.emailInputError?.let {
                FormErrorText(stringResource(it))
            }

            Spacer(modifier = Modifier.height(4.dp))

            PasswordTextField(
                value = state.passwordInputValue,
                onValueChange = { text ->
                    onAction(AuthAction.OnPasswordInputTextChanged(text))
                },
                onDone = {
                    keyboard?.hide()
                    onAction(
                        AuthAction.OnAuthSubmit(state.emailInputValue, state.passwordInputValue)
                    )
                },
                label = { Text(stringResource(Res.string.auth_screen_password_input_label)) },
                placeholder = { PlaceholderText(stringResource(Res.string.auth_screen_password_input_placeholder)) },
                singleLine = true,
                isError = state.passwordInputError != null,
                modifier = Modifier
                    .fillMaxWidth()
            )

            state.passwordInputError?.let {
                FormErrorText(stringResource(it))
            }

            TextButton(
                onClick = {
                    onAction(AuthAction.OnForgetPasswordClicked)
                },
                modifier = Modifier
                    .align(Alignment.End)
            ) {
                Text(
                    stringResource(Res.string.auth_screen_forgot_password),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            LoadingButton(
                onClick = {
                    keyboard?.hide()
                    onAction(
                        AuthAction.OnAuthSubmit(state.emailInputValue, state.passwordInputValue)
                    )
                },
                enabled = !state.onSubmitLoading,
                isLoading = state.onSubmitLoading,
                label = stringResource(Res.string.auth_screen_submit_button),
                shrinkToText = true
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

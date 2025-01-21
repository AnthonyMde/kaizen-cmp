package org.towny.kaizen.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kaizen.composeapp.generated.resources.Res
import kaizen.composeapp.generated.resources.landscape_icon
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.towny.kaizen.ui.screens.components.FormErrorText
import org.towny.kaizen.ui.screens.components.LoadingButton
import org.towny.kaizen.ui.screens.components.PasswordTextField

@Composable
fun AuthScreenRoot(
    viewModel: AuthViewModel = koinViewModel(),
    goToHomeScreen: () -> Unit,
    goToOnboardingProfile: () -> Unit
) {
    val loginScreenState by viewModel.authScreenState.collectAsState()
    val scope = rememberCoroutineScope()
    LaunchedEffect(true) {
        scope.launch {
            viewModel.navigationEvents.collectLatest { event ->
                when (event) {
                    AuthNavigationEvent.GoToHomeScreen -> goToHomeScreen()
                    AuthNavigationEvent.GoToOnboardingProfile -> goToOnboardingProfile()
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

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .imePadding(),
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
            text = "Welcome to Kaizen",
            style = MaterialTheme.typography.headlineMedium,
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = state.emailInputValue,
            onValueChange = { text ->
                onAction(AuthAction.OnEmailInputTextChanged(text))
            },
            label = { Text("Email") },
            placeholder = { Text("kaizen@challenge.com") },
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

        Spacer(modifier = Modifier.height(6.dp))

        state.emailInputError?.let {
            FormErrorText(it)
        }

        PasswordTextField(
            value = state.passwordInputValue,
            onValueChange = { text ->
                onAction(AuthAction.OnPasswordInputTextChanged(text))
            },
            label = { Text("Password") },
            placeholder = { Text("Strong password") },
            singleLine = true,
            isError = state.passwordInputError != null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(6.dp))

        state.passwordInputError?.let {
            FormErrorText(it)
        }

        Spacer(modifier = Modifier.height(16.dp))

        LoadingButton(
            onClick = {
                keyboard?.hide()
                onAction(
                    AuthAction.OnAuthSubmit(state.emailInputValue, state.passwordInputValue)
                )
            },
            enabled = !state.onSubmitLoading,
            isLoading = state.onSubmitLoading,
            label = "Access Kaizen",
            shrinkToText = true
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

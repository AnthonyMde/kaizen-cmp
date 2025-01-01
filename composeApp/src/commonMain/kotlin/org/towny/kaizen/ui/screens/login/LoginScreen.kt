package org.towny.kaizen.ui.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel
import org.towny.kaizen.ui.screens.components.FormErrorText

@Composable
fun LoginScreenRoot(
    viewModel: LoginViewModel = koinViewModel(),
    goToHomeScreen: () -> Unit
) {
    val loginScreenState by viewModel.loginScreenState.collectAsState()
    LaunchedEffect(true) {
        viewModel.navigationEvents.collectLatest { event ->
            when (event) {
                LoginNavigationEvent.GoToHomeScreen -> goToHomeScreen()
            }
        }
    }
    LoginScreen(
        state = loginScreenState,
        onAction = viewModel::onAction,
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.surface)
            .systemBarsPadding()
    )
}

@Composable
fun LoginScreen(
    state: LoginScreenState,
    onAction: (LoginAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .imePadding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        Text(
            text = "Who are you?",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        TextField(
            value = state.loginInput,
            onValueChange = { text ->
                onAction(LoginAction.OnLoginInputTextChanged(text))
            },
            placeholder = { Text("Enter your name") },
            textStyle = MaterialTheme.typography.bodyLarge,
            singleLine = true,
            isError = state.errorMessage != null,
            keyboardOptions = KeyboardOptions().copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    onAction(LoginAction.OnLoginSubmit(state.loginInput))
                }
            ),
            shape = RoundedCornerShape(16.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(6.dp))

        state.errorMessage?.let {
            FormErrorText(state.errorMessage)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                keyboardController?.hide()
                onAction(LoginAction.OnLoginSubmit(state.loginInput))
            },
        ) {
            Text("Access Kaizen")
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (state.onSubmitLoading) {
            CircularProgressIndicator()
        }
    }
}
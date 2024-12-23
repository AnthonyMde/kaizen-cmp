package org.towny.kaizen.ui.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel

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
        action = viewModel::onAction,
    )
}

@Composable
fun LoginScreen(
    state: LoginScreenState,
    action: (LoginAction) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .imePadding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        Text(
            text = "Who are you?",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Normal)
        )
        Spacer(modifier = Modifier.height(24.dp))
        TextField(
            value = state.loginInput,
            onValueChange = { text ->
                action(LoginAction.OnLoginInputTextChanged(text))
            },
            placeholder = { Text("Enter your name") },
            textStyle = MaterialTheme.typography.bodyLarge,
            singleLine = true,
            isError = state.errorMessage != null,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(6.dp))
        if (state.errorMessage != null) {
            Text(
                state.errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        ElevatedButton(
            onClick = {
                keyboardController?.hide()
                action(LoginAction.OnLoginSubmit(state.loginInput))
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
package org.towny.kaizen.ui.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreenRoot() {
    val viewModel: LoginViewModel = koinViewModel()
    val loginScreenState by viewModel.loginScreenState.collectAsState()
    LoginScreen(
        state = loginScreenState,
        action = viewModel::onAction
    )
}

@Composable
fun LoginScreen(
    state: LoginScreenState,
    action: (LoginAction) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
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
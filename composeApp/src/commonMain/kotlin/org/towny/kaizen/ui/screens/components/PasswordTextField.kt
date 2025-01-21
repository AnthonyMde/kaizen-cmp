package org.towny.kaizen.ui.screens.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import kaizen.composeapp.generated.resources.Res
import kaizen.composeapp.generated.resources.icon_visibility
import kaizen.composeapp.generated.resources.icon_visibility_off
import org.jetbrains.compose.resources.painterResource

@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    singleLine: Boolean,
    isError: Boolean,
    onDone: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = { text ->
            onValueChange(text)
        },
        label = label,
        placeholder = placeholder,
        singleLine = singleLine,
        isError = isError,
        keyboardOptions = KeyboardOptions().copy(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                onDone()
            }
        ),
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(
                onClick = { isPasswordVisible = !isPasswordVisible },
                content = {
                    val icon = if (isPasswordVisible) Res.drawable.icon_visibility else Res.drawable.icon_visibility_off
                    val description = if (isPasswordVisible) "Hide password." else "Show password."
                    Icon(
                        painter = painterResource(icon),
                        contentDescription = description
                    )
                }
            )
        },
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
    )
}

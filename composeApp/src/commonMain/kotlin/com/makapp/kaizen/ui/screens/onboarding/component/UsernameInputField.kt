package com.makapp.kaizen.ui.screens.onboarding.component

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.makapp.kaizen.ui.screens.onboarding.OnBoardingProfileAction
import kaizen.composeapp.generated.resources.Res
import kaizen.composeapp.generated.resources.onboarding_profile_screen_username_input_label
import kaizen.composeapp.generated.resources.onboarding_profile_screen_username_support_text
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun UsernameInputField(
    value: String,
    error: StringResource?,
    onAction: (OnBoardingProfileAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    TextField(
        value = value,
        onValueChange = { username ->
            onAction(OnBoardingProfileAction.OnUsernameInputValueChanged(username))
        },
        label = { Text(stringResource(Res.string.onboarding_profile_screen_username_input_label)) },
        singleLine = true,
        supportingText = {
            val stringRes = error
                ?: Res.string.onboarding_profile_screen_username_support_text
            Text(stringResource(stringRes))
        },
        isError = error != null,
        trailingIcon = {
            if (error != null) Icon(
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
        modifier = modifier
    )
}
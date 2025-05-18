package com.makapp.kaizen.ui.screens.challenge_details.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.makapp.kaizen.ui.components.LoadingButton
import com.makapp.kaizen.ui.screens.challenge_details.ChallengeDetailsAction
import kaizen.composeapp.generated.resources.Res
import kaizen.composeapp.generated.resources.challenge_details_forgot_to_check_button
import org.jetbrains.compose.resources.stringResource

@Composable
fun ForgotChallengeButtonView(
    challengeId: String,
    isLoading: Boolean,
    onAction: (ChallengeDetailsAction) -> Unit,
) {
    Spacer(modifier = Modifier.height(16.dp))
    LoadingButton(
        onClick = {
            onAction(
                ChallengeDetailsAction.OnForgotToCheckButtonClicked(challengeId))
        },
        enabled = !isLoading,
        isLoading = isLoading,
        label = stringResource(Res.string.challenge_details_forgot_to_check_button),
        shrinkToText = true,
        buttonColors = ButtonDefaults.buttonColors().copy(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
        )
    )
}
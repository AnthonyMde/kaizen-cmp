package com.makapp.kaizen.ui.screens.challenge_details.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.makapp.kaizen.domain.models.Challenge
import com.makapp.kaizen.ui.screens.challenge_details.ChallengeDetailsAction
import kaizen.composeapp.generated.resources.Res
import kaizen.composeapp.generated.resources.challenge_details_bs_abandoned_button
import kaizen.composeapp.generated.resources.challenge_details_bs_paused_button
import kaizen.composeapp.generated.resources.challenge_details_bs_resumed_button
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeChallengeStatusBottomSheet(
    onAction: (ChallengeDetailsAction) -> Unit,
    currentStatus: Challenge.Status
) {
    ModalBottomSheet(
        onDismissRequest = {
            onAction(ChallengeDetailsAction.OnBottomSheetDismissed)
        },
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
        ) {
            Button(
                onClick = {
                    onAction(getAction(currentStatus))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(getTitle(currentStatus))
            }
            TextButton(
                onClick = {
                    onAction(ChallengeDetailsAction.OnGiveUpChallengeClicked)
                },
                colors = ButtonDefaults.textButtonColors().copy(
                    contentColor = MaterialTheme.colorScheme.error
                ),
                modifier = Modifier
            ) {
                Text(stringResource(Res.string.challenge_details_bs_abandoned_button))
            }
        }
    }
}

@Composable
private fun getTitle(status: Challenge.Status): String {
    return if (status === Challenge.Status.ON_GOING) {
        stringResource(Res.string.challenge_details_bs_paused_button)
    } else {
        stringResource(Res.string.challenge_details_bs_resumed_button)
    }
}

private fun getAction(status: Challenge.Status): ChallengeDetailsAction {
    return if (status === Challenge.Status.ON_GOING) {
        ChallengeDetailsAction.OnPauseChallengeClicked
    } else {
        ChallengeDetailsAction.OnResumeChallengeClicked
    }
}

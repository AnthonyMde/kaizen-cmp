package com.makapp.kaizen.ui.screens.create_challenge.infos

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.makapp.kaizen.ui.components.LimitedCharTextField
import com.makapp.kaizen.ui.components.Stepper
import com.makapp.kaizen.ui.screens.components.BackTopAppBar
import com.makapp.kaizen.ui.screens.components.LoadingButton
import com.makapp.kaizen.ui.screens.components.PlaceholderText
import com.makapp.kaizen.ui.screens.create_challenge.CreateChallengeFunnelState
import com.makapp.kaizen.ui.screens.create_challenge.CreateChallengeNavigationEvent
import com.makapp.kaizen.ui.screens.create_challenge.CreateChallengeViewModel
import kaizen.composeapp.generated.resources.Res
import kaizen.composeapp.generated.resources.ic_info_outlined
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

@Composable
fun CreateChallengeInfosScreenRoot(
    viewModel: CreateChallengeViewModel,
    navArgs: ChallengeInfosNavArgs,
    navigateUp: () -> Unit,
    goToExpectationsStep: () -> Unit,
) {
    val state by viewModel.createChallengeScreenState.collectAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(null) {
        scope.launch {
            viewModel.navigationEvents.collectLatest { event ->
                when (event) {
                    CreateChallengeNavigationEvent.GoToExpectationsStep -> goToExpectationsStep()
                    CreateChallengeNavigationEvent.NavigateUp -> navigateUp()
                    else -> {}
                }
            }
        }

        // TODO: do better by passing args directly to VM.
        if (!navArgs.title.isNullOrBlank()) {
            viewModel.onInfosAction(CreateChallengeInfosAction.OnNameInputValueChanged(navArgs.title))
        }
        if (navArgs.isEditing && navArgs.lives != null) {
            viewModel.onInfosAction(CreateChallengeInfosAction.SetMinimumNumberOfLives(navArgs.lives))
            viewModel.onInfosAction(CreateChallengeInfosAction.OnNumberOfLivesChanged(navArgs.lives))
        }
    }

    CreateChallengeInfos(
        state = state,
        navArgs = navArgs,
        onAction = { action ->
            when (action) {
                CreateChallengeInfosAction.OnNavigateUp -> navigateUp()
                else -> viewModel.onInfosAction(action)
            }
        }
    )
}

@Composable
fun CreateChallengeInfos(
    state: CreateChallengeFunnelState,
    navArgs: ChallengeInfosNavArgs,
    onAction: (CreateChallengeInfosAction) -> Unit
) {
    val keyboard = LocalSoftwareKeyboardController.current
    val focus = LocalFocusManager.current
    val scroll = rememberScrollState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            BackTopAppBar(
                title = "General infos",
                onNavigateUp = {
                    onAction(CreateChallengeInfosAction.OnNavigateUp)
                },
                backDescription = "Go back to account.",
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(null) {
                    detectTapGestures(
                        onTap = { focus.clearFocus() }
                    )
                }
                .padding(innerPadding)
                .imePadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .verticalScroll(scroll)
                    .padding(horizontal = 24.dp)
                    .padding(top = 8.dp, bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title field.
                LimitedCharTextField(
                    onValueChange = { name ->
                        onAction(CreateChallengeInfosAction.OnNameInputValueChanged(name))
                    },
                    value = state.challengeNameInputValue,
                    maxCharAllowed = CreateChallengeViewModel.MAX_CHALLENGE_TITLE_LENGTH,
                    textError = state.challengeNameInputError,
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    label = {
                        Text("Title")
                    },
                    placeholder = {
                        PlaceholderText("Reading")
                    },
                    isError = state.challengeNameInputError != null,
                    keyboardOptions = KeyboardOptions().copy(
                        imeAction = ImeAction.Next,
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        "Number of lives",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Stepper(
                        value = state.numberOfLivesValue,
                        onIncrease = { lives ->
                            onAction(CreateChallengeInfosAction.OnNumberOfLivesChanged(lives))
                        },
                        onDecrease = { lives ->
                            onAction(CreateChallengeInfosAction.OnNumberOfLivesChanged(lives))
                        },
                        max = CreateChallengeViewModel.MAX_LIVES_ALLOWED,
                        min = state.minimumLives
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.tertiaryContainer)
                        .padding(16.dp)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_info_outlined),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = if (navArgs.isEditing) {
                            "You can only increase your number of lives (max 12)."
                        } else
                            "Your lives represent how many times you can miss your challenge (max. 12).",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                LoadingButton(
                    onClick = {
                        submit(keyboard, onAction, navArgs.isEditing, navArgs.challengeId)
                    },
                    enabled = state.challengeNameInputValue.isNotBlank() &&
                            !state.isUpdateInfosLoading,
                    isLoading = state.isUpdateInfosLoading,
                    label = if (navArgs.isEditing) "Update" else "Next",
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
}

private fun submit(
    keyboard: SoftwareKeyboardController?,
    onAction: (CreateChallengeInfosAction) -> Unit,
    editing: Boolean,
    challengeId: String?
) {
    keyboard?.hide()
    if (editing && challengeId != null) {
        onAction(CreateChallengeInfosAction.OnUpdateInfos(challengeId))
    } else if (!editing) {
        onAction(CreateChallengeInfosAction.GoToCommitmentStep)
    }
}

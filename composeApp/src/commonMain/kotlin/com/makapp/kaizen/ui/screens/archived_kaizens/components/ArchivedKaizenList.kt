package com.makapp.kaizen.ui.screens.archived_kaizens.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.makapp.kaizen.domain.models.challenge.Challenge
import com.makapp.kaizen.ui.screens.archived_kaizens.ArchivedKaizensAction
import kaizen.composeapp.generated.resources.Res
import kaizen.composeapp.generated.resources.archived_kaizens_abandoned_section_title
import kaizen.composeapp.generated.resources.archived_kaizens_failed_section_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun ArchivedKaizenList(
    failedKaizens: List<Challenge>,
    abandonedKaizens: List<Challenge>,
    onAction: (ArchivedKaizensAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val abandonedSectionTitle = stringResource(Res.string.archived_kaizens_abandoned_section_title)
    val failedSectionTitle = stringResource(Res.string.archived_kaizens_failed_section_title
    )
    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (abandonedKaizens.isNotEmpty()) {
            archivedKaizenSection(
                title = abandonedSectionTitle,
                kaizens = abandonedKaizens,
                onAction = onAction,
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (failedKaizens.isNotEmpty()) {
            archivedKaizenSection(
                title = failedSectionTitle,
                kaizens = failedKaizens,
                onAction = onAction,
            )
        }
    }
}

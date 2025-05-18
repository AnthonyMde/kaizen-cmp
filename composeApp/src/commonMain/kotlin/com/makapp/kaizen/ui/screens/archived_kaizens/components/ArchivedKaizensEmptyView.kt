package com.makapp.kaizen.ui.screens.archived_kaizens.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kaizen.composeapp.generated.resources.Res
import kaizen.composeapp.generated.resources.archived_kaizens_empty_view_subtitle
import kaizen.composeapp.generated.resources.archived_kaizens_empty_view_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun ArchivedKaizensEmptyView(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            alignment = Alignment.CenterVertically,
            space = 16.dp
        )
    ) {
        Text(
            stringResource(Res.string.archived_kaizens_empty_view_title),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
        )
        Text(
            stringResource(Res.string.archived_kaizens_empty_view_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
        )
    }
}
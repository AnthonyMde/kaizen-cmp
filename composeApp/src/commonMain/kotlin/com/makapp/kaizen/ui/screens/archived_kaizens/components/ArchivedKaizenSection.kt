package com.makapp.kaizen.ui.screens.archived_kaizens.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.makapp.kaizen.domain.models.challenge.Challenge
import com.makapp.kaizen.ui.screens.archived_kaizens.ArchivedKaizensAction
import com.makapp.kaizen.ui.screens.home.components.ChallengeView

fun LazyListScope.archivedKaizenSection(
    title: String,
    kaizens: List<Challenge>,
    onAction: (ArchivedKaizensAction) -> Unit
) {
    item {
        Text(
            text = title,
        )
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
    }

    items(kaizens) { kaizen ->
        ChallengeView(
            challenge = kaizen,
            belongToCurrentUser = true,
            onRowClick = {
                onAction(ArchivedKaizensAction.GoToChallengeDetails(kaizen.id))
            },
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}
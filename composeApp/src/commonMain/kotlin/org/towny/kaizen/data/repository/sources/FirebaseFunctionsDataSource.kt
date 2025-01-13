package org.towny.kaizen.data.repository.sources

import org.towny.kaizen.domain.models.FriendPreview

interface FirebaseFunctionsDataSource {
    suspend fun getFriendPreviewByName(username: String): FriendPreview
}

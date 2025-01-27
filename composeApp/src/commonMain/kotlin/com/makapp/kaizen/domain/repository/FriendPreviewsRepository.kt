package com.makapp.kaizen.domain.repository

import com.makapp.kaizen.domain.models.FriendPreview
import com.makapp.kaizen.domain.models.FriendSearchPreview
import com.makapp.kaizen.domain.models.Resource
import kotlinx.coroutines.flow.Flow

interface FriendPreviewsRepository {
    suspend fun getFriendSearchPreview(username: String): Resource<FriendSearchPreview>
    fun watchFriendPreviews(): Flow<Resource<List<FriendPreview>>>
    suspend fun refreshFriendPreviews(): Resource<Unit>
}

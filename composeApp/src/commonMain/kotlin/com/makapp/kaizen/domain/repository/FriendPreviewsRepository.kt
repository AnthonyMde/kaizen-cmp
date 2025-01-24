package com.makapp.kaizen.domain.repository

import com.makapp.kaizen.domain.models.FriendPreview
import com.makapp.kaizen.domain.models.Resource
import kotlinx.coroutines.flow.Flow

interface FriendPreviewsRepository {
    suspend fun getFriendPreview(username: String): Resource<FriendPreview>
    fun watchFriendPreviews(): Flow<Resource<List<FriendPreview>>>
    suspend fun refreshFriendPreviews(): Resource<Unit>
}

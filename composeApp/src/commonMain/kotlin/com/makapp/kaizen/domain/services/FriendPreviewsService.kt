package com.makapp.kaizen.domain.services

import com.makapp.kaizen.domain.models.friend.FriendPreview
import com.makapp.kaizen.domain.models.Resource
import com.makapp.kaizen.domain.repository.FriendPreviewsRepository
import kotlinx.coroutines.flow.Flow

class FriendPreviewsService(
    private val friendPreviewsRepository: FriendPreviewsRepository
) {
    fun watchFriendPreviews(): Flow<Resource<List<FriendPreview>>> {
        return friendPreviewsRepository.watchFriendPreviews()
    }

    suspend fun refreshFriendPreviews() {
        friendPreviewsRepository.refreshFriendPreviews()
    }
}

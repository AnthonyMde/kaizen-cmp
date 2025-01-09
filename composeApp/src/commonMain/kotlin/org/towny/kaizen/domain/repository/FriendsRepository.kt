package org.towny.kaizen.domain.repository

import org.towny.kaizen.domain.models.FriendRequest
import org.towny.kaizen.domain.models.Resource

interface FriendsRepository {
    suspend fun createOrUpdateFriendRequest(request: FriendRequest): Resource<Unit>
}

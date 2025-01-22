package com.makapp.kaizen.domain.repository

import com.makapp.kaizen.domain.models.Friend
import com.makapp.kaizen.domain.models.FriendPreview
import com.makapp.kaizen.domain.models.Resource

interface FriendsRepository {
    suspend fun getFriendPreview(username: String): Resource<FriendPreview>
    suspend fun getFriendPreviews(): Resource<List<FriendPreview>>
    suspend fun getFriends(): Resource<List<Friend>>
}
